package org.hubay.byteandbuy.config;

import lombok.Getter;

/**
 * Obsahuje hodnoty nacitane z application.properties.
 */
@Getter
public class GameConfig {
    private final int startBonus;
    private final int startMoney;
    private final int minPlayersToStart;
    private final double fullGroupRentMultiplier;
    private final double workshopDiscount;
    private final double initialPriceMinMultiplier;
    private final double initialPriceMaxMultiplier;
    private final double priceMinMultiplier;
    private final double priceMaxMultiplier;
    private final double baseRentRate;
    private final double rentForOnePropertyMultiplier;

    public GameConfig(int startBonus, int startMoney, int minPlayersToStart,
                      double fullGroupRentMultiplier, double workshopDiscount, double initialPriceMinMultiplier,
                      double initialPriceMaxMultiplier, double priceMinMultiplier, double priceMaxMultiplier,
                      double baseRentRate, double rentForOnePropertyMultiplier) {
        this.startBonus = startBonus;
        this.startMoney = startMoney;
        this.minPlayersToStart = minPlayersToStart;
        this.fullGroupRentMultiplier = fullGroupRentMultiplier;
        this.workshopDiscount = workshopDiscount;
        this.initialPriceMinMultiplier = initialPriceMinMultiplier;
        this.initialPriceMaxMultiplier = initialPriceMaxMultiplier;
        this.priceMinMultiplier = priceMinMultiplier;
        this.priceMaxMultiplier = priceMaxMultiplier;
        this.baseRentRate = baseRentRate;
        this.rentForOnePropertyMultiplier = rentForOnePropertyMultiplier;
    }
}
