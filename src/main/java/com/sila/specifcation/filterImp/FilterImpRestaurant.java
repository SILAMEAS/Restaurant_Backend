package com.sila.specifcation.filterImp;

import com.sila.dto.request.RestaurantReq;
import com.sila.dto.request.SearchReq;
import com.sila.specifcation.RestaurantSpecification;
import com.sila.model.Address;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.AddressRepository;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Objects;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilterImpRestaurant {
    private AddressRepository addressRepository;
    private UserService userService;
    private static RestaurantService restaurantService;
    public static Specification<Restaurant> filterRestaurant(SearchReq searchReq){
        Specification<Restaurant> spec = Specification.where(null);
        if (Objects.nonNull(searchReq.getSearch())) {
            spec = spec.and(RestaurantSpecification.likeNameOrDescription(searchReq.getSearch()));
        }
        return spec;
    }
    public Restaurant handleCreateRestaurant(RestaurantReq req, Long userId) throws Exception {
        Address address = addressRepository.save(req.getAddress());
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setDescription(req.getDescription());
        restaurant.setRegistrationDate(LocalDateTime.now());
        User user = userService.findUserById(userId);
        restaurant.setOwner(user);
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setImages(req.getImages());
        return restaurant;
    }

    public static Restaurant handleUpdateRestaurant(RestaurantReq updateRestaurant) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(updateRestaurant.getId());
        if (!Objects.isNull(updateRestaurant.getName())) {
            restaurant.setName(updateRestaurant.getName());
        }
        if (!Objects.isNull(updateRestaurant.getDescription())) {
            restaurant.setDescription(updateRestaurant.getDescription());
        }
        if (!Objects.isNull(updateRestaurant.getCuisineType())) {
            restaurant.setCuisineType(updateRestaurant.getCuisineType());
        }
        if (!Objects.isNull(updateRestaurant.getImages())) {
            restaurant.setImages(updateRestaurant.getImages());
        }
        if (!Objects.isNull(updateRestaurant.getImages())) {
            restaurant.setImages(updateRestaurant.getImages());
        }
        restaurant.setOpen(updateRestaurant.isOpen());
        return restaurant;
    }
}
