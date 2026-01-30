package com.sila.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.detail")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ApplicationProperties {
    private String name;
    private String description;
    private String version;
}