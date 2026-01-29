package com.sila.config.modelMapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
