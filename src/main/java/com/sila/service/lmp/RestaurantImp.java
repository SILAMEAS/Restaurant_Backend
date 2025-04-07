package com.sila.service.lmp;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
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
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import com.sila.specifcation.filterImp.FilterImpRestaurant;
import com.sila.utlis.Util;
import com.sila.utlis.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    @Override
    public Restaurant createRestaurant(RestaurantRequest restaurantReq) {
        var userId = UserContext.getUser().getId();
        boolean existsByOwner = restaurantRepository.existsByOwnerId(userId);
        if (existsByOwner) {
            throw new BadRequestException("User have restaurant ready!");
        }
        Address address = addressRepository.save(restaurantReq.getAddress());
        Restaurant restaurant = Restaurant.builder().address(address).name(restaurantReq.getName()).openingHours(restaurantReq.getOpeningHours())
                .description(restaurantReq.getDescription()).registrationDate(LocalDateTime.now()).build();
        User user = userService.findUserById(userId);
        restaurant.setOwner(user);
        restaurant.setCuisineType(restaurantReq.getCuisineType());
        restaurant.setContactInformation(restaurantReq.getContactInformation());
        restaurant.setImages(restaurantReq.getImages());
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public Restaurant updateRestaurant(RestaurantRequest updateRestaurant, Long restaurantId) throws Exception {
        var userId = UserContext.getUser().getId();
        Restaurant restaurantExit = handleFindRestaurantById(restaurantId);
        if (!isUserOwnerOfRestaurant(userId, restaurantId)) {
            throw new AccessDeniedException("You are not the owner of this restaurant.");
        }
        Util.setIfNotNull(updateRestaurant.getName(), restaurantExit::setName);
        Util.setIfNotNull(updateRestaurant.getDescription(), restaurantExit::setDescription);
        Util.setIfNotNull(updateRestaurant.getCuisineType(), restaurantExit::setCuisineType);
        Util.setIfNotNull(updateRestaurant.getImages(), restaurantExit::setImages);
        Util.setIfNotNull(updateRestaurant.getAddress(), restaurantExit::setAddress);
        Util.setIfNotNull(updateRestaurant.getOpeningHours(), restaurantExit::setOpeningHours);
        Util.setIfNotNull(updateRestaurant.getContactInformation(), restaurantExit::setContactInformation);
        Util.setIfNotNull(updateRestaurant.isOpen(), restaurantExit::setOpen);
        restaurantRepository.save(restaurantExit);
        return restaurantExit;
    }

    @Override
    public void deleteRestaurant(Long id) throws Exception {
        Restaurant isRestaurantExit = findRestaurantById(id);
        restaurantRepository.delete(isRestaurantExit);
    }


    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Restaurant not found with id " + id));
    }

    @Override
    public Restaurant getRestaurantByUserId() {
        var userId = UserContext.getUser().getId();
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if (Objects.isNull(restaurant)) {
            throw new BadRequestException("user don't have restaurant");
        }
        return restaurant;
    }

    @Override
    public EntityResponseHandler<RestaurantResponse> searchRestaurant(Pageable pageable,
                                                                      SearchRequest searchReq) {
        var restaurantPage = restaurantRepository.findAll(FilterImpRestaurant.filterRestaurant(searchReq), pageable);
        return new EntityResponseHandler<>(restaurantPage
                .map(restaurant -> this.modelMapper.map(restaurant, RestaurantResponse.class)));
    }

    @Override
    public List<FavoriteResponse> addRestaurantToFavorites(Long restaurantId, User user) throws Exception {
        // Fetch the restaurant to ensure it's managed
        Restaurant findRestaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Create a new Favorite instance
        Favorite fav = new Favorite();
        fav.setName(findRestaurant.getName());
        fav.setDescription(findRestaurant.getDescription());
        fav.setRestaurant(findRestaurant); // Set the restaurant
        fav.setOwner(user); // Set the owner

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
        userRepository.save(user);

        return userService.getUserProfile().getFavourites();
    }

    @Override
    public Restaurant updateRestaurantStatus(Long restaurantId) throws Exception {
        Restaurant findRestaurant = findRestaurantById(restaurantId);
        findRestaurant.setOpen(!findRestaurant.isOpen());
        restaurantRepository.save(findRestaurant);
        return restaurantRepository.save(findRestaurant);
    }

    public boolean isUserOwnerOfRestaurant(Long userId, Long restaurantId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        return restaurant.getOwner() != null && restaurant.getOwner().getId().equals(userId);
    }

    public Restaurant handleFindRestaurantById(Long restaurantId) throws Exception {
        Optional<Restaurant> restaurantExit = restaurantRepository.findById(restaurantId);
        if (restaurantExit.isPresent()) {
            return restaurantExit.get();
        }
        throw new NotFoundException("Not Found Restaurant with id : " + restaurantId);
    }
}
