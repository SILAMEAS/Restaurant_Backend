package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.FoodRequest;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.exception.BadRequestException;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.model.image.ImageFood;
import com.sila.model.image.ImageRestaurant;
import com.sila.repository.CategoryRepository;
import com.sila.repository.FoodRepository;
import com.sila.repository.OrderItemRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.service.CloudinaryService;
import com.sila.service.FoodService;
import com.sila.service.RestaurantService;
import com.sila.specifcation.FoodSpecification;
import com.sila.util.PageableUtil;
import com.sila.util.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.sila.specifcation.FoodSpecification.filterByFoodType;
import static com.sila.specifcation.FoodSpecification.filterByPrice;
import static com.sila.specifcation.FoodSpecification.filterByPriceRange;
import static com.sila.specifcation.FoodSpecification.filterCategory;

@Service
@RequiredArgsConstructor
public class FoodImp implements FoodService {

    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantService restaurantService;

    @Override
    public Food create(FoodRequest foodRequest, Category category, Restaurant restaurant, List<MultipartFile> imageFiles) {
        Food food = Food.builder().name(foodRequest.getName()).description(foodRequest.getDescription()).price(foodRequest.getPrice()).available(foodRequest.isAvailable()).foodtype(foodRequest.getFoodType()).creationDate(new Date()).category(category).restaurant(restaurant).build();

        List<ImageFood> imageEntities = cloudinaryService.uploadImagesToCloudinary(imageFiles,food,(url,publicId)->{
            ImageFood image = new ImageFood();
            image.setUrl(url);
            image.setPublicId(publicId);
            return image;
        },ImageFood::setFood);

        food.setImages(imageEntities);
        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);
        return savedFood;
    }

    @Override
    public Food update(FoodRequest foodReq, Long foodId) {
        var user = UserContext.getUser();
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        Food foodToUpdate = getById(foodId);
        if (!foodToUpdate.getRestaurant().getId().equals(restaurant.getId())) {
            throw new BadRequestException("Food isn't belong to restaurant");
        }
        Category category = categoryRepository.findById(foodReq.getCategoryId()).orElseThrow(() -> new BadRequestException("category not found"));

        Food food = getById(foodId);
        Utils.setIfNotNull(foodReq.getName(), food::setName);
        Utils.setIfNotNull(category, food::setCategory);
        Utils.setIfNotNull(foodReq.getPrice(), food::setPrice);
        Utils.setIfNotNull(foodReq.getDescription(), food::setDescription);
        Utils.setIfNotNull(foodReq.getFoodType(), food::setFoodtype);
        Utils.setIfNotNull(foodReq.isAvailable(), food::setAvailable);

        cloudinaryService.updateEntityImages(
                food,
                foodReq.getImages(),
                Food::getImages,
                Food::setImages,
                (url, publicId) -> {
                    ImageFood image = new ImageFood();
                    image.setUrl(url);
                    image.setPublicId(publicId);
                    return image;
                },
                ImageFood::setFood,
                ImageFood::getPublicId
        );


        return foodRepository.save(food);
    }



    @Override
    public void delete(Long id) {
        Food food = getById(id);
        var orderItems = orderItemRepository.findAllByFood(food);
        if(orderItems.isEmpty()){
            food.setRestaurant(null);
            foodRepository.deleteById(id);
        }
        throw new BadRequestException("Food has been order! finished order before delete");
    }

    @Override
    @Transactional
    public String deleteByCategoryId(Long categoryId) {
        var foods = foodRepository.findAllByCategoryId(categoryId);
        for(var food : foods){
            orderItemRepository.deleteAllByFood(food);
        }
        foodRepository.deleteAllByCategoryId(categoryId);
        return "All food have categoryId : " + categoryId + " was deleted";
    }

    @Override
    public Food getById(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new BadRequestException("Not found food with this id"));
    }

    @Override
    public Food updateStatus(Long id) {
        Food updateFood = getById(id);
        updateFood.setAvailable(!updateFood.isAvailable());
        return foodRepository.save(updateFood);
    }

    @Override
    public EntityResponseHandler<FoodResponse> gets(PaginationRequest request) {
        Pageable pageable = PageableUtil.fromRequest(request);
        Specification<Food> spec = Specification.where(null);
        if (Objects.nonNull(request.getFilterBy())) {
            spec = spec.and(filterCategory(request.getFilterBy()));
        }
        if (Objects.nonNull(request.getPrice())) {
            spec = spec.and(filterByPrice(request.getPrice()));
        }
        if (Objects.nonNull(request.getFoodType())) {
            spec = spec.and(filterByFoodType(request.getFoodType()));
        }
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            spec = spec.and(filterByPriceRange(request.getMinPrice(), request.getMaxPrice()));
        }
        Page<FoodResponse> page = foodRepository
                .findAll(spec, pageable)
                .map(FoodResponse::toResponse); // keeps pagination metadata
        return new EntityResponseHandler<>(page);
    }

    @Override
    public Long all() {
        return foodRepository.count();
    }

    @Override
    public Long all(Long restaurantId) throws Exception {
        var restaurant = restaurantService.getById(restaurantId);
        return foodRepository.countAllByRestaurant(restaurant);
    }

    @Override
    public EntityResponseHandler<FoodResponse> getsByResId(Long restaurantId,  PaginationRequest request) {
        Pageable pageable = PageableUtil.fromRequest(request);
        var foodPage = foodRepository.findAll(FoodSpecification.filterFoodByRestaurantId(restaurantId, request.getFilterBy()), pageable);
        return new EntityResponseHandler<>(foodPage.map(fs -> this.modelMapper.map(fs, FoodResponse.class)));
    }

}
