package org.hubay.byteandbuy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Nacita hodnoty z application.properties.
 */
@Configuration
public class GameConfiguration {
    @Value("${game.startBonus}")
    private int startBonus;
    @Value("${gameBuilder.startMoney}")
    private int startMoney;
    @Value("${game.minPlayersToStart}")
    private int minPlayersToStart;
    @Value("${gameBuilder.fullGroupRentMultiplier}")
    private double fullGroupRentMultiplier;
    @Value("${economyService.workshopDiscount}")
    private double workshopDiscount;
    @Value("${gameBuilder.initialPriceMinMultiplier}")
    private double initialPriceMinMultiplier;
    @Value("${gameBuilder.initialPriceMaxMultiplier}")
    private double initialPriceMaxMultiplier;
    @Value("${market.marketService.minPriceMultiplier}")
    private double minPriceMultiplier;
    @Value("${market.marketService.maxPriceMultiplier}")
    private double maxPriceMultiplier;
    @Value("${gameBuilder.baseRentRate}")
    private double baseRentRate;
    @Value("${gameBuilder.rentForOnePropertyMultiplier}")
    private double rentForOnePropertyMultiplier;

    @Bean
    public GameConfig gameConfig() {
        return new GameConfig(startBonus, startMoney, minPlayersToStart, fullGroupRentMultiplier,
                workshopDiscount, initialPriceMinMultiplier, initialPriceMaxMultiplier,
                minPriceMultiplier, maxPriceMultiplier, baseRentRate, rentForOnePropertyMultiplier);
    }
}
