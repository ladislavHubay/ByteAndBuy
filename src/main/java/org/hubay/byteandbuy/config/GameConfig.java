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

    public GameConfig(int startBonus, int startMoney, int minPlayersToStart,
                      double fullGroupRentMultiplier, double workshopDiscount) {
        this.startBonus = startBonus;
        this.startMoney = startMoney;
        this.minPlayersToStart = minPlayersToStart;
        this.fullGroupRentMultiplier = fullGroupRentMultiplier;
        this.workshopDiscount = workshopDiscount;
    }
}
