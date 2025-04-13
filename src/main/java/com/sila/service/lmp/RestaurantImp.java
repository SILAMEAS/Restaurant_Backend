package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.RestaurantRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.RestaurantResponse;
import com.sila.exception.AccessDeniedException;
import com.sila.exception.BadRequestException;
import com.sila.exception.NotFoundException;
import com.sila.model.Address;
import com.sila.model.Favorite;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.AddressRepository;
import com.sila.repository.FavoriteRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.repository.UserRepository;
import com.sila.service.CloudinaryService;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import com.sila.specifcation.RestaurantSpecification;
import com.sila.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    @Override
    public Restaurant create(RestaurantRequest restaurantReq, List<MultipartFile> imageRestaurants) {
        var userId = UserContext.getUser().getId();
        boolean existsByOwner = restaurantRepository.existsByOwnerId(userId);
        if (existsByOwner) {
            throw new BadRequestException("User have restaurant ready!");
        }
        Address address = addressRepository.save(restaurantReq.getAddress());
        Restaurant restaurant = Restaurant.builder().address(address).name(restaurantReq.getName()).openingHours(restaurantReq.getOpeningHours()).description(restaurantReq.getDescription()).registrationDate(LocalDateTime.now()).build();
        User user = userService.getById(userId);
        var imageEntities = cloudinaryService.uploadRestaurantImageToCloudinary(imageRestaurants, restaurant);
        restaurant.setOwner(user);
        restaurant.setCuisineType(restaurantReq.getCuisineType());
        restaurant.setContactInformation(restaurantReq.getContactInformation());
        restaurant.setImages(imageEntities);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public Restaurant update(RestaurantRequest updateRestaurant, Long restaurantId, List<MultipartFile> imageRestaurants) throws Exception {
        var userId = UserContext.getUser().getId();
        Restaurant restaurant = handleFindRestaurantById(restaurantId);
        if (!isUserOwnerOfRestaurant(userId, restaurantId)) {
            throw new AccessDeniedException("You are not the owner of this restaurant.");
        }
        var imageEntities = cloudinaryService.uploadRestaurantImageToCloudinary(imageRestaurants, restaurant);
        Utils.setIfNotNull(updateRestaurant.getName(), restaurant::setName);
        Utils.setIfNotNull(updateRestaurant.getDescription(), restaurant::setDescription);
        Utils.setIfNotNull(updateRestaurant.getCuisineType(), restaurant::setCuisineType);
        restaurant.setImages(imageEntities);
        Utils.setIfNotNull(updateRestaurant.getAddress(), restaurant::setAddress);
        Utils.setIfNotNull(updateRestaurant.getOpeningHours(), restaurant::setOpeningHours);
        Utils.setIfNotNull(updateRestaurant.getContactInformation(), restaurant::setContactInformation);
        Utils.setIfNotNull(updateRestaurant.isOpen(), restaurant::setOpen);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public void delete(Long id) throws Exception {
        Restaurant isRestaurantExit = getById(id);
        restaurantRepository.delete(isRestaurantExit);
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
        return restaurant.getOwner() != null && restaurant.getOwner().getId().equals(userId);
    }

    public Restaurant handleFindRestaurantById(Long restaurantId) {
        Optional<Restaurant> restaurantExit = restaurantRepository.findById(restaurantId);
        if (restaurantExit.isEmpty()) {
            throw new NotFoundException("Not Found Restaurant with id : " + restaurantId);
        }
        return restaurantExit.get();
    }
}
