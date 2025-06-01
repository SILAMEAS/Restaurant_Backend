package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.CategoryRequest;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.CategoryResponse;
import com.sila.dto.response.MessageResponse;
import com.sila.exception.AccessDeniedException;
import com.sila.exception.BadRequestException;
import com.sila.model.Category;
import com.sila.model.User;
import com.sila.repository.CategoryRepository;
import com.sila.repository.FoodRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.service.CategoryService;
import com.sila.service.CloudinaryService;
import com.sila.service.FoodService;
import com.sila.service.RestaurantService;
import com.sila.util.PageableUtil;
import com.sila.util.Utils;
import com.sila.util.enums.KeyImageProperty;
import com.sila.util.enums.ROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;
    final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final RestaurantService restaurantService;

    @Override
    public Category create(CategoryRequest request) {
        User user = UserContext.getUser();
        var restaurant =restaurantService.findRestaurantByOwner(user);
        if (categoryRepository.existsByNameAndRestaurant(request.getName(), restaurant)) {
            throw new BadRequestException("Category name already exists for this restaurant");
        }
        var image =Boolean.TRUE.equals(request.getRemoveBg())?  cloudinaryService.uploadFileRemoveBG(request.getImage()): cloudinaryService.uploadFile(request.getImage());

        Category category = Category.builder()
                .name(request.getName())
                .restaurant(restaurant)
                .url(image.get("secureUrl"))
                .publicId(image.get("publicId"))
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category update(CategoryRequest request, Long categoryId) {
        if (UserContext.getUser().getRole().equals(ROLE.USER)) {
            throw new AccessDeniedException("Customer do not have permission to change category");
        }
        Category category = getById(categoryId);
        if(request.getImage()!=null){
            var image = cloudinaryService.uploadFile(request.getImage());
            Utils.setIfNotNull(image.get(KeyImageProperty.url.toString()),category::setUrl);
            Utils.setIfNotNull(image.get(KeyImageProperty.publicId.toString()),category::setPublicId);
        }


        Utils.setIfNotNull(request.getName(),category::setName);

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
            throw new BadRequestException("Please remove food from category before delete category");
//            foodService.deleteByCategoryId(categoryId);
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
        var user = UserContext.getUser();

        Long restaurantId=null;
        if(user.getRole().equals(ROLE.OWNER)){
            var restaurantExit  = restaurantService.findRestaurantByOwner(user);
            restaurantId = user.getRole()==ROLE.OWNER ? restaurantExit.getId() : null;
        }
        Pageable pageable = PageableUtil.fromRequest(request);
        Page<Category> categoryPage = categoryRepository.findByFilters(
                request.getSearch(),
                restaurantId,
                pageable
        );

        return new EntityResponseHandler<>(categoryPage.map(CategoryImp::mapToCategoryResponse));
    }

    @Override
    public String deleteAllCategories() {
        var images = categoryRepository.findAll().stream().map(Category::getPublicId).toList();
        cloudinaryService.deleteImages(images);
        log.info("Successfully deleted all images belong categories from cloudinary. Images : " + images + " deleted successfully.");
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

    public static CategoryResponse mapToCategoryResponse(Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .items(!CollectionUtils.isEmpty(category.getFoodList())?category.getFoodList().size():0)
                .restaurant(category.getRestaurant().getName())
                .url(category.getUrl())
                .publicId(category.getPublicId())
                .name(category.getName())
                .build();
    }

}
