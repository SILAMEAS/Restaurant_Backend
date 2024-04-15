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
import com.sila.repository.CategoryRepository;
import com.sila.repository.FoodRepository;
import com.sila.service.CategoryService;
import com.sila.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FoodImp implements FoodService {

  private final FoodRepository foodRepository;
  private final CategoryRepository categoryRepository;
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
    food_create.setAvailable(food.isAvailable());
//    food_create.setIngredientsItems(food.getIngredients());
    Food saveFood = foodRepository.save(food_create);
    restaurant.getFoods().add(saveFood);
    return saveFood;
  }

  @Override
  public Food updateFood(FoodReq food,Long foodId) throws Exception {
    Food foodExited = findFoodById(foodId);
    Category category= categoryRepository.findById(food.getCategoryId()).orElseThrow(()->new BadRequestException("category not found"));
    if(!Objects.isNull(food.getName())){
      foodExited.setName(food.getName());
    }
    if(!Objects.isNull(food.getImages())){
      foodExited.setImages(food.getImages());
    }
    if(!Objects.isNull(food.getCategoryId())){
      foodExited.setFoodCategory(category);
    }
    if(!Objects.isNull(food.getPrice())){
      foodExited.setPrice(food.getPrice());
    }
    if(!Objects.isNull(food.getDescription())){
      foodExited.setDescription(food.getDescription());
    }
      foodExited.setVegetarian(food.isVegetarin());
      foodExited.setSeasonal(food.isSeasional());
      foodExited.setAvailable(food.isAvailable());

      return foodRepository.save(foodExited);
  }

  @Override
  public Void deleteFoodById(Long id) throws Exception {
    Food foodByID = findFoodById(id);
    foodByID.setRestaurant(null);
    foodRepository.deleteById(id);
    return null;
  }

  @Override
  public String deleteFoodByCategoryId(Long categoryId) throws Exception {
    List<Food> foodsToDelete = foodRepository.findAllByFoodCategoryId(categoryId);
    if(foodsToDelete.isEmpty()){
      throw new BadRequestException("No food have category id : "+categoryId);
    }
    foodRepository.deleteAll(foodsToDelete);
    return "All food have categoryId : "+categoryId+" was deleted";
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
