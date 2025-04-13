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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public RestaurantResponse create(RestaurantRequest restaurantReq) {
        var user = UserContext.getUser();
        if (restaurantRepository.existsByOwnerId(user.getId())) {
            throw new BadRequestException("User already owns a restaurant!");
        }

        Address address = addressRepository.save(Address.builder()
                .streetAddress(restaurantReq.getAddress().getStreetAddress())
                .city(restaurantReq.getAddress().getCity())
                .country(restaurantReq.getAddress().getCountry())
                .stateProvince(restaurantReq.getAddress().getStateProvince())
                .postalCode(restaurantReq.getAddress().getPostalCode())
                .user(user)
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
        return mapToResponse(savedRestaurant);
    }


    @Override
    public Restaurant update(RestaurantRequest updateRestaurant, Long restaurantId) throws Exception {
        var userId = UserContext.getUser().getId();
        Restaurant restaurant = handleFindRestaurantById(restaurantId);
        if (!isUserOwnerOfRestaurant(userId, restaurantId)) {
            throw new AccessDeniedException("You are not the owner of this restaurant.");
        }
        if(updateRestaurant.getImages()!=null) {
            var imageEntities = cloudinaryService.uploadRestaurantImageToCloudinary(updateRestaurant.getImages(), restaurant);
            restaurant.setImages(imageEntities);
        }
        Utils.setIfNotNull(updateRestaurant.getName(), restaurant::setName);
        Utils.setIfNotNull(updateRestaurant.getDescription(), restaurant::setDescription);
        Utils.setIfNotNull(updateRestaurant.getCuisineType(), restaurant::setCuisineType);
        Utils.setIfNotNull(updateRestaurant.getAddress(), restaurant::setAddress);
        Utils.setIfNotNull(updateRestaurant.getOpeningHours(), restaurant::setOpeningHours);
        Utils.setIfNotNull(updateRestaurant.getContactInformation(), restaurant::setContactInformation);
        Utils.setIfNotNull(updateRestaurant.getOpen(), restaurant::setOpen);
        return restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public MessageResponse delete(Long id) throws Exception {
        getById(id);
        // First delete categories related to the restaurant
        categoryRepository.deleteByRestaurantId(id); // custom method you'll need

        // Now delete the restaurant
        restaurantRepository.deleteById(id);

        return MessageResponse.builder().message("Deleted restaurant id: " + id + " successfully!").status(HttpStatus.OK.value()).build();
    }


    @Override
    public Restaurant getById(Long id) throws Exception {
        return restaurantRepository.findById(id).orElseThrow(() -> new BadRequestException("Restaurant not found with id " + id));
    }

    @Override
    public Restaurant getByUserLogin() {
        var userId = UserContext.getUser().getId();
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if (Objects.isNull(restaurant)) {
            throw new BadRequestException("user don't have restaurant");
        }
        return restaurant;
    }

    @Override
    public EntityResponseHandler<RestaurantResponse> search(Pageable pageable, SearchRequest searchReq) {
        var restaurantPage = restaurantRepository.findAll(RestaurantSpecification.filterRestaurant(searchReq), pageable);
        return new EntityResponseHandler<>(restaurantPage.map(restaurant -> this.modelMapper.map(restaurant, RestaurantResponse.class)));
    }

    @Override
    public List<FavoriteResponse> addFav(Long restaurantId, User user) throws Exception {
        // Fetch the restaurant to ensure it's managed
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Create a new Favorite instance
        Favorite fav = Favorite.builder().name(restaurant.getName()).description(restaurant.getDescription()).restaurant(restaurant).owner(user).build();
        boolean isFavorite = false;
        List<Favorite> favorites = user.getFavourites();

        // Check if the restaurant is already in the favorites
        for (Favorite favorite : favorites) {
            if (favorite.getRestaurant().getId().equals(restaurantId)) {
                isFavorite = true;
                break;
            }
        }

        if (isFavorite) {
            // Remove the favorite if it already exists
            favorites.removeIf(f -> f.getRestaurant().getId().equals(restaurantId));
        } else {
            // Add the new favorite
            favorites.add(fav);
        }
        // Save the user (this should also save the favorites if cascade is set correctly)
        favoriteRepository.saveAll(favorites);
        user.setFavourites(favorites);
        userRepository.save(user);

        return userService.getProfile().getFavourites();
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
    private RestaurantResponse mapToResponse(Restaurant restaurant) {
        List<String> imageUrls = restaurant.getImages().stream()
                .map(ImageRestaurant::getUrl)
                .toList();

        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .cuisineType(restaurant.getCuisineType())
                .open(restaurant.isOpen())
                .openingHours(restaurant.getOpeningHours())
                .registrationDate(restaurant.getRegistrationDate())
                .address(AddressResponse.builder()
                        .streetAddress(restaurant.getAddress().getStreetAddress())
                        .city(restaurant.getAddress().getCity())
                        .country(restaurant.getAddress().getCountry())
                        .postalCode(restaurant.getAddress().getPostalCode())
                        .stateProvince(restaurant.getAddress().getStateProvince())
                        .build())
                .contactInformation(ContactInformationResponse.builder()
                        .email(restaurant.getContactInformation().getEmail())
                        .phone(restaurant.getContactInformation().getPhone())
                        .build())
                .imageUrls(imageUrls)
                .build();
    }
}
