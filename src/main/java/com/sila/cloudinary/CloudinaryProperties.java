package com.sila.cloudinary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryProperties {
    private String cloudName;
    private String apiKey;
    private String apiSecret;
}
