package org.hubay.byteandbuy.config;

import lombok.Getter;

@Getter
public class GameConfig {
    private final int startBonus;
    private final int startMoney;
    private final double fullGroupRentMultiplier;

    public GameConfig(int startBonus, int startMoney, double fullGroupRentMultiplier) {
        this.startBonus = startBonus;
        this.startMoney = startMoney;
        this.fullGroupRentMultiplier = fullGroupRentMultiplier;
    }
}
