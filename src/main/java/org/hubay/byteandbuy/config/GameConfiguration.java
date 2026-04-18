package org.hubay.byteandbuy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfiguration {
    @Value("${game.startBonus}")
    private int startBonus;
    @Value("${game.startMoney}")
    private int startMoney;
    @Value("${game.fullGroupRentMultiplier}")
    private double fullGroupRentMultiplier;
    @Value("${game.workshopDiscount}")
    private double workshopDiscount;

    @Bean
    public GameConfig gameConfig() {
        return new GameConfig(startBonus, startMoney, fullGroupRentMultiplier, workshopDiscount);
    }
}
