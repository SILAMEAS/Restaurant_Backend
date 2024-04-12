package com.sila.lmp;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.RestaurantReq;
import com.sila.dto.request.SearchReq;
import com.sila.dto.response.RestaurantFavRes;
import com.sila.dto.response.RestaurantRes;
import com.sila.exception.BadRequestException;
import com.sila.lmp.filterImp.FilterImpRestaurant;
import com.sila.model.Address;
import com.sila.model.Favorite;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.AddressRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.repository.UserRepository;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class RestaurantImp implements RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final UserService userService;
  private final AddressRepository addressRepository;
  @Override
  public Restaurant createRestaurant(RestaurantReq req,String jwt) throws Exception {
    User user =  userService.findUserByJwtToken(jwt);
    Restaurant userHaveRes= restaurantRepository.findByOwnerId(user.getId());
    if(Objects.nonNull(userHaveRes)){
      throw new BadRequestException("User have restaurant ready!");
    }
    return restaurantRepository.save(handleCreateRestaurant(req,user.getId()));
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

  @Override
  public Restaurant updateRestaurant(RestaurantReq updateRestaurant) throws Exception {
    return restaurantRepository.save(handleUpdateRestaurant(updateRestaurant));
  }
  public  Restaurant handleUpdateRestaurant(RestaurantReq updateRestaurant) throws Exception {
    Restaurant restaurant = findRestaurantById(updateRestaurant.getId());
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
  public Restaurant getRestaurantByUserId(Long userId) {
    Restaurant restaurant= restaurantRepository.findByOwnerId(userId);
    if(Objects.isNull(restaurant)){
      throw new BadRequestException("user don't have restaurant");
    }
    return restaurant;
  }

  @Override
  public EntityResponseHandler<RestaurantRes> searchRestaurant(Pageable pageable,
      SearchReq searchReq) {
    var restaurantPage=restaurantRepository.findAll(FilterImpRestaurant.filterRestaurant(searchReq), pageable);
    return new EntityResponseHandler<>(restaurantPage
        .map(restaurant -> this.modelMapper.map(restaurant, RestaurantRes.class)));
  }
  @Override
  public User addRestaurantToFavorites(Long restaurantId, User user) throws Exception {
//    Restaurant findRestaurant = findRestaurantById(restaurantId);
//    Favorite fav = new Favorite();
//    fav.setId(findRestaurant.getId());
//    fav.setName(findRestaurant.getName());
//    fav.setDescription(findRestaurant.getDescription());
//    boolean isFavorite = false;
//    List<Favorite> favorites = user.getFavourites();
//    for (Favorite favorite : favorites) {
//      if (favorite.getId().equals(restaurantId)) {
//        isFavorite = true;
//        break;
//      }
//
//    }
//    if (isFavorite) {
//      favorites.removeIf(f -> f.getId().equals(restaurantId));
//    } else {
//      favorites.add(fav);
//    }
//    userRepository.save(user);
    return user;
  }

  @Override
  public Restaurant updateRestaurantStatus(Long restaurantId) throws Exception {
    Restaurant findRestaurant = findRestaurantById(restaurantId);
    findRestaurant.setOpen(!findRestaurant.isOpen());
    restaurantRepository.save(findRestaurant);
    return restaurantRepository.save(findRestaurant);
  }



}
