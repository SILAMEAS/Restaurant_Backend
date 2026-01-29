package com.sila;

import com.sila.config.cors.CorsProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@OpenAPIDefinition(
        info = @Info(
                title = "Online Food Application",
                version = "1.0",
                description = "API documentation for Online Food Application"
        )
)
@SpringBootApplication
@EnableJpaAuditing // 👈 enable auditing
@EnableConfigurationProperties(CorsProperties.class)
@ConfigurationPropertiesScan
public class OnlineFoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineFoodApplication.class, args);
    }

}
