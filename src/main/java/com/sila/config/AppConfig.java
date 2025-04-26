package com.sila.config;

import com.sila.config.context.UserContextFilter;
import com.sila.config.jwt.JwtTokenValidator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class AppConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigrationSource()))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/auth/**" // allow login/register
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll() // Allow GET requests to /api/**
                        .requestMatchers("/api/admin/**").hasAnyAuthority("OWNER", "ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Store Information fo User Login
     */
    public FilterRegistrationBean<UserContextFilter> userContextFilter(UserContextFilter filter) {
        FilterRegistrationBean<UserContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(1); // early in the chain
        return registrationBean;
    }
//    CorsConfiguration cfg = new CorsConfiguration();
//            cfg.setAllowedOrigins(List.of("http://localhost:3000", "https://sila-restrurant.vercel.app",
//                    "http://localhost:3030", "http://192.168.1.8:3030", "http://192.168.1.8:3000"));
//            cfg.setAllowedMethods(Collections.singletonList("*"));
//            cfg.setAllowCredentials(true);
//            cfg.setAllowedHeaders(Collections.singletonList("*"));
//            cfg.setExposedHeaders(List.of("Authorization"));
//            cfg.setMaxAge(3600L);

    private CorsConfigurationSource corsConfigrationSource() {
        return request -> {
            CorsConfiguration cfg = new CorsConfiguration();
            // Allow all origins using wildcard
            cfg.setAllowedOrigins(Collections.singletonList("*"));
            // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
            cfg.setAllowedMethods(Collections.singletonList("*"));
            // Allow credentials (e.g., cookies, authorization headers)
//            cfg.setAllowCredentials(true);
            // Allow all headers
            cfg.setAllowedHeaders(Collections.singletonList("*"));
            // Expose specific headers to the client
            cfg.setExposedHeaders(List.of("Authorization"));
            // Set the max age for preflight requests
            cfg.setMaxAge(3600L);
            return cfg;
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
