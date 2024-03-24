package com.hust.config;

import com.hust.validator.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OidcConfig {
    @Bean
    public UserValidator userValidator() {
        return new UserValidator();
    }
}
