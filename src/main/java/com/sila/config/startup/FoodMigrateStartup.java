package com.sila.config.startup;

import com.sila.modules.food.model.Food;
import com.sila.modules.food.repository.FoodRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class FoodMigrateStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final FoodRepository foodRepository;

    public FoodMigrateStartup(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Food> foods = foodRepository.findAll();
        if (CollectionUtils.isEmpty(foods)) {
            return;
        }

        List<Food> toUpdateFoods = new ArrayList<>();

        for (Food food : foods) {
            if (food.getPrice() != null) {
                double calculated = food.getPriceWithDiscountCalculated();
                food.setPriceWithDiscount(calculated);
                toUpdateFoods.add(food);
            }
        }

        if (!CollectionUtils.isEmpty(toUpdateFoods)) {
            foodRepository.saveAll(toUpdateFoods);
        }
    }
}