package com.sila.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ddz6bkk0m",
                "api_key", "675499539668193",
                "api_secret", "32PfiKAo7glmYYLM5MOoXZ6KS0Q",
                "secure", true
        ));
    }
}
