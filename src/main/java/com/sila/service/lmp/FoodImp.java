package com.sila.service.lmp;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.FoodRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.exception.BadRequestException;
import com.sila.specifcation.filterImp.FilterImpFood;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.repository.CategoryRepository;
import com.sila.repository.FoodRepository;
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
  public Food createFood(FoodRequest food, Category category, Restaurant restaurant)
      throws Exception {
    Food food_create = Food.builder().category(category).name(food.getName()).
            restaurant(restaurant).description(food.getDescription()).images(food.getImages()).
            name(food.getName()).price(food.getPrice()).creationDate(new Date()).isSeasonal(food.isSeasonal()).isVegetarian(food.isVegetarian())
            .available(food.isAvailable())
            .build();
    Food saveFood = foodRepository.save(food_create);
    restaurant.getFoods().add(saveFood);
    return saveFood;
  }

  @Override
  public Food updateFood(FoodRequest foodReq, Long foodId) throws Exception {
    Category category= categoryRepository.findById(foodReq.getCategoryId()).orElseThrow(()->new BadRequestException("category not found"));
    Food food=findFoodById(foodId);
    food=Food.builder().name(Objects.isNull(foodReq.getName())?foodReq.getName():food.getName()).description(foodReq.getDescription()).images(foodReq.getImages()).build();
//    food.
//    if(!Objects.isNull(food.getName())){
//      foodExited.setName(food.getName());
//    }
//    if(!Objects.isNull(food.getImages())){
//      foodExited.setImages(food.getImages());
//    }
//    if(!Objects.isNull(food.getCategoryId())){
//      foodExited.setCategory(category);
//    }
//    if(!Objects.isNull(food.getPrice())){
//      foodExited.setPrice(food.getPrice());
//    }
//    if(!Objects.isNull(food.getDescription())){
//      foodExited.setDescription(food.getDescription());
//    }
//      foodExited.setVegetarian(food.isVegetarian());
//      foodExited.setSeasonal(food.isSeasonal());
//      foodExited.setAvailable(food.isAvailable());
//
      return foodRepository.save(food);
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
    List<Food> foodsToDelete = foodRepository.findAllByCategoryId(categoryId);
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
  public EntityResponseHandler<FoodResponse> listFoods(Pageable pageable, SearchRequest searchReq, String filterBy) {
    var foodPage = foodRepository.findAll(FilterImpFood.filterFood(searchReq,filterBy), pageable);
    return new EntityResponseHandler<>(foodPage
            .map(fs->this.modelMapper.map(fs, FoodResponse.class)));
  }
  @Override
  public EntityResponseHandler<FoodResponse> listFoodsByRestaurantId(Long restaurantId, Pageable pageable, SearchRequest searchReq, String filterBy) {
    var foodPage = foodRepository.findAll(FilterImpFood.filterFoodByRestaurantId(restaurantId,searchReq,filterBy), pageable);
    return new EntityResponseHandler<>(foodPage
            .map(fs->this.modelMapper.map(fs, FoodResponse.class)));
  }
}
