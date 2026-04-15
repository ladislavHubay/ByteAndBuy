package org.hubay.byteandbuy.config;

import lombok.Getter;

@Getter
public class GameConfig {
    private final int startBonus;

    public GameConfig(int startBonus) {
        this.startBonus = startBonus;
    }
}
