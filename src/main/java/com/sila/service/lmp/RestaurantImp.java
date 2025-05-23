package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.RestaurantRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.*;
import com.sila.exception.AccessDeniedException;
import com.sila.exception.BadRequestException;
import com.sila.exception.NotFoundException;
import com.sila.model.Address;
import com.sila.model.Favorite;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.model.image.ImageRestaurant;
import com.sila.repository.*;
import com.sila.service.CloudinaryService;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import com.sila.specifcation.RestaurantSpecification;
import com.sila.util.Utils;
import com.sila.util.enums.ROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class RestaurantImp implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final CloudinaryService cloudinaryService;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public RestaurantResponse create(RestaurantRequest restaurantReq) {
        var user = UserContext.getUser();
        if (restaurantRepository.existsByOwnerId(user.getId())) {
            throw new BadRequestException("User already owns a restaurant!");
        }

        Address address = addressRepository.save(Address.builder()
                .name(restaurantReq.getAddress().getName()) // ✅ Add this line
                .street(restaurantReq.getAddress().getStreet())
                .city(restaurantReq.getAddress().getCity())
                .country(restaurantReq.getAddress().getCountry())
                .state(restaurantReq.getAddress().getState())
                .zip(restaurantReq.getAddress().getZip())
                .user(user).currentUsage(restaurantReq.getAddress().getCurrentUsage())
                .build());

        Restaurant restaurant = Restaurant.builder()
                .name(restaurantReq.getName())
                .openingHours(restaurantReq.getOpeningHours())
                .description(restaurantReq.getDescription())
                .registrationDate(LocalDateTime.now())
                .owner(user)
                .cuisineType(restaurantReq.getCuisineType())
                .address(address)
                .contactInformation(restaurantReq.getContactInformation())
                .open(restaurantReq.getOpen()) // don't forget this!
                .build();

        var imageEntities = cloudinaryService.uploadRestaurantImageToCloudinary(restaurantReq.getImages(), restaurant);
        restaurant.setImages(imageEntities);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return mapToRestaurantResponse(savedRestaurant);
    }


    @Override
    public RestaurantResponse update(RestaurantRequest updateRestaurant, Long restaurantId) throws Exception {
        var userId = UserContext.getUser().getId();
        Restaurant restaurant = handleFindRestaurantById(restaurantId);
        if (!isUserOwnerOfRestaurant(userId, restaurantId)) {
            throw new AccessDeniedException("You are not the owner of this restaurant.");
        }
        if(updateRestaurant.getImages()!=null) {
            var imageEntities = cloudinaryService.uploadRestaurantImageToCloudinary(updateRestaurant.getImages(), restaurant);
            imageEntities.forEach(restaurant::addImage);
        }
        Utils.setIfNotNull(updateRestaurant.getName(), restaurant::setName);
        Utils.setIfNotNull(updateRestaurant.getDescription(), restaurant::setDescription);
        Utils.setIfNotNull(updateRestaurant.getCuisineType(), restaurant::setCuisineType);
        Utils.setIfNotNull(updateRestaurant.getAddress(), restaurant::setAddress);
        Utils.setIfNotNull(updateRestaurant.getOpeningHours(), restaurant::setOpeningHours);
        Utils.setIfNotNull(updateRestaurant.getContactInformation(), restaurant::setContactInformation);
        Utils.setIfNotNull(updateRestaurant.getOpen(), restaurant::setOpen);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return mapToRestaurantResponse(savedRestaurant);
    }

    @Override
    @Transactional
    public MessageResponse delete(Long id) throws Exception {
        var restaurant = getById(id);
        var images = restaurant.getImages().stream().map(ImageRestaurant::getPublicId).collect(Collectors.toList());
        // First delete categories related to the restaurant
        cloudinaryService.deleteImages(images);

        categoryRepository.deleteByRestaurantId(id); // custom method you'll need

        favoriteRepository.deleteByRestaurantId(id);
        // Now delete the restaurant
        restaurantRepository.deleteById(id);

        return MessageResponse.builder().message("Deleted restaurant id: " + id + " successfully!").status(HttpStatus.OK.value()).build();
    }


    @Override
    public Restaurant getById(Long id) throws Exception {
        return restaurantRepository.findById(id).orElseThrow(() -> new BadRequestException("Restaurant not found with id " + id));
    }

    @Override
    public RestaurantResponse getByUserLogin() {
        var user = UserContext.getUser();
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        if (Objects.isNull(restaurant)) {
            if(user.getRole() == ROLE.OWNER){
                throw new BadRequestException("user don't have restaurant");
            }else
                throw new BadRequestException("this role can't have restaurant");
        }
        return mapToRestaurantResponse(restaurant);
    }

    @Override
    public EntityResponseHandler<RestaurantResponse> search(Pageable pageable, SearchRequest searchReq) {
        var restaurantPage = restaurantRepository.findAll(RestaurantSpecification.filterRestaurant(searchReq), pageable);
        return new EntityResponseHandler<>(restaurantPage.map(RestaurantImp::mapToRestaurantResponse));
    }

    @Override
    public List<FavoriteResponse> addFav(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Favorite fav = Favorite.builder().name(restaurant.getName()).description(restaurant.getDescription()).restaurant(restaurant).owner(UserContext.getUser()).build();
        if (favoriteRepository.existsFavoriteByRestaurantId(restaurantId)) {
            favoriteRepository.deleteByRestaurantId(restaurantId);
        } else {
            restaurant.setFavorites(List.of(fav));
            favoriteRepository.save(fav);
        }

        return restaurant.getFavorites().stream().map(favorite -> modelMapper.map(favorite, FavoriteResponse.class)).toList();
    }


    public boolean isUserOwnerOfRestaurant(Long userId, Long restaurantId) throws Exception {
        Restaurant restaurant = getById(restaurantId);
        if (!restaurant.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("You are not the owner of this restaurant.");
        }
        return true;
    }

    public Restaurant handleFindRestaurantById(Long restaurantId) {
        Optional<Restaurant> restaurantExit = restaurantRepository.findById(restaurantId);
        if (restaurantExit.isEmpty()) {
            throw new NotFoundException("Not Found Restaurant with id : " + restaurantId);
        }
        return restaurantExit.get();
    }
    public static RestaurantResponse mapToRestaurantResponse(Restaurant restaurant) {
        List<ImageDetailsResponse> imageDetails = restaurant.getImages().stream()
                .map(image -> new ImageDetailsResponse(image.getUrl(), image.getPublicId()))
                .toList();

        AddressResponse response = null;
        if (restaurant.getAddress() != null) {
            response = AddressResponse.builder()
                    .name(restaurant.getAddress().getName())
                    .id(restaurant.getAddress().getId())
                    .street(restaurant.getAddress().getStreet())
                    .city(restaurant.getAddress().getCity())
                    .country(restaurant.getAddress().getCountry())
                    .zip(restaurant.getAddress().getZip())
                    .state(restaurant.getAddress().getState())
                    .currentUsage(restaurant.getAddress().getCurrentUsage())
                    .build();
        }

        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .cuisineType(restaurant.getCuisineType())
                .open(restaurant.isOpen())
                .openingHours(restaurant.getOpeningHours())
                .registrationDate(restaurant.getRegistrationDate())
                .address(response)
                .contactInformation(ContactInformationResponse.builder()
                        .email(restaurant.getContactInformation().getEmail())
                        .phone(restaurant.getContactInformation().getPhone())
                        .build())
                .imageUrls(imageDetails)
                .ownerName(restaurant.getOwner().getFullName())
                .build();
    }
}
