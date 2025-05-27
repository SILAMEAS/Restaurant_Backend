package com.sila.config;

import com.sila.dto.response.CategoryResponse;
import com.sila.dto.response.RestaurantResponse;
import com.sila.model.Category;
import com.sila.model.Restaurant;
import com.sila.model.image.ImageRestaurant;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Slf4j
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        final var mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return mapper;
    }

}
