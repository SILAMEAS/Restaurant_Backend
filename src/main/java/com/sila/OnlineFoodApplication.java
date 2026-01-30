package com.sila;

import com.sila.config.properties.ApplicationProperties;
import com.sila.config.properties.CorsProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition
@SpringBootApplication
@EnableJpaAuditing // 👈 enable auditing
@EnableConfigurationProperties({CorsProperties.class, ApplicationProperties.class})
@ConfigurationPropertiesScan
public class OnlineFoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineFoodApplication.class, args);
    }

}
