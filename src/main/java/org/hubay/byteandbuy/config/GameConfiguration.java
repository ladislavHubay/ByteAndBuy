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
    @Value("${game.startMoney}")
    private int startMoney;
    @Value("${game.minPlayersToStart}")
    private int minPlayersToStart;
    @Value("${game.fullGroupRentMultiplier}")
    private double fullGroupRentMultiplier;
    @Value("${game.workshopDiscount}")
    private double workshopDiscount;
    @Value("${game.market.initialPriceMinMultiplier}")
    private double initialPriceMinMultiplier;
    @Value("${game.market.initialPriceMaxMultiplier}")
    private double initialPriceMaxMultiplier;
    @Value("${game.market.minPriceMultiplier}")
    private double priceMinMultiplier;
    @Value("${game.market.maxPriceMultiplier}")
    private double priceMaxMultiplier;
    @Value("${game.property.baseRentRate}")
    private double baseRentRate;
    @Value("${game.property.rentForOnePropertyMultiplier}")
    private double rentForOnePropertyMultiplier;

    @Bean
    public GameConfig gameConfig() {
        return new GameConfig(startBonus, startMoney, minPlayersToStart, fullGroupRentMultiplier,
                workshopDiscount, initialPriceMinMultiplier, initialPriceMaxMultiplier,
                priceMinMultiplier, priceMaxMultiplier, baseRentRate, rentForOnePropertyMultiplier);
    }
}
