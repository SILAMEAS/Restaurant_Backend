package com.sila.lmp;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.FoodReq;
import com.sila.dto.request.SearchReq;
import com.sila.dto.response.FoodRes;
import com.sila.exception.BadRequestException;
import com.sila.lmp.filterImp.FilterImpFood;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.repository.FoodRepository;
import com.sila.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class FoodImp implements FoodService {

  private final FoodRepository foodRepository;
  private final ModelMapper modelMapper;

  @Override
  public Food createFood(FoodReq food, Category category, Restaurant restaurant)
      throws Exception {
    Food food_create = new Food();
    food_create.setFoodCategory(category);
    food_create.setRestaurant(restaurant);
    food_create.setDescription(food.getDescription());
    food_create.setImages(food.getImages());
    food_create.setName(food.getName());
    food_create.setPrice(food.getPrice());
    food_create.setCreationDate(new Date());
    food_create.setSeasonal(food.isSeasional());
    food_create.setVegetarian(food.isVegetarin());
//    food_create.setIngredientsItems(food.getIngredients());
    Food saveFood = foodRepository.save(food_create);
    restaurant.getFoods().add(saveFood);
    return saveFood;
  }

  @Override
  public Void deleteFoodById(Long id) throws Exception {
    Food foodByID = findFoodById(id);
    foodByID.setRestaurant(null);
    foodRepository.deleteById(id);
    return null;
  }

  @Override
  public Food findFoodById(Long foodId) throws Exception {
    return foodRepository.findById(foodId).orElseThrow(()->new BadRequestException("NOT FOUND"));
  }

  @Override
  public Food updateAvailibilityStatus(Long id) throws Exception {
    Food updateFood = findFoodById(id);
    updateFood.setAvailable(!updateFood.isAvailable());
    return foodRepository.save(updateFood);
  }

  @Override
  public EntityResponseHandler<FoodRes> listFoods(Pageable pageable, SearchReq searchReq,String filterBy) {
    var foodPage = foodRepository.findAll(FilterImpFood.filterFood(searchReq,filterBy), pageable);
    return new EntityResponseHandler<>(foodPage
            .map(fs->this.modelMapper.map(fs,FoodRes.class)));
  }
  @Override
  public EntityResponseHandler<FoodRes> listFoodsByRestaurantId(Long restaurantId,Pageable pageable, SearchReq searchReq,String filterBy) {
    var foodPage = foodRepository.findAll(FilterImpFood.filterFoodByRestaurantId(restaurantId,searchReq,filterBy), pageable);
    return new EntityResponseHandler<>(foodPage
            .map(fs->this.modelMapper.map(fs,FoodRes.class)));
  }
}
