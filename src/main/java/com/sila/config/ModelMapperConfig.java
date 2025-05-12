package com.sila.config;

import com.sila.dto.response.RestaurantResponse;
import com.sila.model.Restaurant;
import com.sila.model.image.ImageRestaurant;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        final var mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
//        mapper.createTypeMap(Restaurant.class, RestaurantResponse.class)
//                .addMapping(restaurant -> restaurant.getImages().stream().map(ImageRestaurant::getUrl).toList(), RestaurantResponse::setImageUrls);
        return mapper;
    }
}
