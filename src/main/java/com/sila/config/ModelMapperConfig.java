package com.sila.config;

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
    return mapper;
  }
}
