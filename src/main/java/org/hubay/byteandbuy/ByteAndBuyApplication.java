package org.hubay.byteandbuy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spusta Spring Boot aplikaciu.
 */
@SpringBootApplication
public class ByteAndBuyApplication {

    public static void main(String[] args) {
        // http://localhost:8080/game/create
        SpringApplication.run(ByteAndBuyApplication.class, args);
    }
}
