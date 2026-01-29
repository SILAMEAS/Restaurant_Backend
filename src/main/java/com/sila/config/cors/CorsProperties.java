package com.sila.config.cors;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.cors")
@Setter
@Getter
public class CorsProperties {
    private List<String> allowedOrigins;
}