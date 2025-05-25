package com.sila.service.lmp;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.CategoryRequest;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.CategoryResponse;
import com.sila.dto.response.FoodResponse;
import com.sila.dto.response.MessageResponse;
import com.sila.exception.AccessDeniedException;
import com.sila.exception.BadRequestException;
import com.sila.model.Category;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.CategoryRepository;
import com.sila.repository.FoodRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.service.CategoryService;
import com.sila.service.CloudinaryService;
import com.sila.service.FoodService;
import com.sila.config.context.UserContext;
import com.sila.specifcation.FoodSpecification;
import com.sila.util.PageableUtil;
import com.sila.util.enums.ROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodService foodService;
    private final FoodRepository foodRepository;
    final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    @Override
    public Category create(CategoryRequest request) {
        User user = UserContext.getUser();
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        if (categoryRepository.existsByNameAndRestaurant(request.getName(), restaurant)) {
            throw new BadRequestException("Category name already exists for this restaurant");
        }
        var image = cloudinaryService.uploadFile(request.getImage());
        Category category = Category.builder()
                .name(request.getName())
                .restaurant(restaurant)
                .url(image.get("secureUrl"))
                .publicId(image.get("publicId"))
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category update(String name, Long categoryId) {
        ROLE user = UserContext.getUser().getRole();
        if (user.equals(ROLE.USER)) {
            throw new AccessDeniedException("Customer do not have permission to change category");
        }
        Category category = getById(categoryId);
        if (!name.isEmpty()) {
            category.setName(name);
        }
        return categoryRepository.save(category);
    }


    @Override
    public List<Category> getsByResId(Long restaurantId) {
        return categoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public MessageResponse delete(Long categoryId) {
        var category =getById(categoryId);
        var foods = foodRepository.findAllByCategoryId(category.getId());
        if (!foods.isEmpty()) {
            foodService.deleteByCategoryId(categoryId);
        }
        categoryRepository.deleteById(categoryId);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Category Id : " + categoryId + " successfully!");
        return messageResponse;
    }

    @Override
    public List<CategoryResponse> all() {
        var categories = categoryRepository.findAll();
      return categories.stream().map(c->modelMapper.map(c,CategoryResponse.class)).toList();
    }

    @Override
    public EntityResponseHandler<CategoryResponse> gets(PaginationRequest request) {
        Pageable pageable = PageableUtil.fromRequest(request);
        Page<Category> categoryPage = categoryRepository.findByFilters(
                request.getSearch(),
                parseRestaurantId(request.getFilterBy()),
                pageable
        );

        return new EntityResponseHandler<>(categoryPage.map(c -> modelMapper.map(c, CategoryResponse.class)));
    }

    @Override
    public String deleteAllCategories() {
        categoryRepository.deleteAll();
        return "Deleted all categories successfully";
    }

    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new BadRequestException("category not found"));
    }

    private Long parseRestaurantId(String filterBy) {
        try {
            return filterBy != null ? Long.parseLong(filterBy) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
