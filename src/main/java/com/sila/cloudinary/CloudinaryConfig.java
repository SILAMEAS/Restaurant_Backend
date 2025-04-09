package com.sila.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    private final CloudinaryProperties cloudinaryProperties;

    public CloudinaryConfig(CloudinaryProperties cloudinaryProperties) {
        this.cloudinaryProperties = cloudinaryProperties;
    }

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = ObjectUtils.asMap(
                "cloud_name", cloudinaryProperties.getCloudName(),
                "api_key", cloudinaryProperties.getApiKey(),
                "api_secret", cloudinaryProperties.getApiSecret(),
                "secure", true
        );
        return new Cloudinary(config);
    }
}
