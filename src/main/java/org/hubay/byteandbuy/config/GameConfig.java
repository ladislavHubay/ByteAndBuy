package org.hubay.byteandbuy.config;

import lombok.Getter;

@Getter
public class GameConfig {
    private final int startBonus;
    private final int startMoney;
    private final double fullGroupRentMultiplier;
    private final double workshopDiscount;

    public GameConfig(int startBonus, int startMoney, double fullGroupRentMultiplier, double workshopDiscount) {
        this.startBonus = startBonus;
        this.startMoney = startMoney;
        this.fullGroupRentMultiplier = fullGroupRentMultiplier;
        this.workshopDiscount = workshopDiscount;
    }
}
