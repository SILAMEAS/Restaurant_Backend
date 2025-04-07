package com.sila;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Online Food Application",
                version = "1.0",
                description = "API documentation for Online Food Application"
        )
)
@SpringBootApplication
public class OnlineFoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineFoodApplication.class, args);
    }

}
