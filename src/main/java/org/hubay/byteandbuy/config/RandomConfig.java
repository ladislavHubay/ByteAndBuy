package org.hubay.byteandbuy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * Pouziva sa na generovanie nahodnych cisel.
 */
@Configuration
public class RandomConfig {
    @Bean
    public Random random() {
        return new Random();
    }
}
