package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.config.GameConfig;
import org.hubay.byteandbuy.config.RandomConfig;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.tiles.Companies;
import org.hubay.byteandbuy.model.tiles.PropertyGroup;
import org.springframework.stereotype.Service;

/**
 * Simuluje vyvoj cien firiem na trhu.
 */
@Service
public class MarketService {
    private final RandomConfig randomConfig;
    private final GameConfig gameConfig;

    public MarketService(RandomConfig randomConfig, GameConfig gameConfig) {
        this.randomConfig = randomConfig;
        this.gameConfig = gameConfig;
    }

    /**
     * Aktualizuje ceny firiem.
     */
    public void updateCompanyPrices(Game game){
        Companies companies = game.getCompanies();

        for (PropertyGroup company : companies.getGroups()) {
            updateValue(company);
        }
    }

    /**
     * Vypocita novu cenu firmy na trhu.
     * Cena sa moze pohybovat iba v definovanom rozsahu.
     */
    private void updateValue(PropertyGroup company){
        int price = company.getInitialPrice();
        int minPrice = (int)(price * gameConfig.getPriceMinMultiplier());
        int maxPrice = (int)(price * gameConfig.getPriceMaxMultiplier());

        int newPrice = calculateNewPrice(company.getCurrentPrice(), minPrice, maxPrice);

        company.setCurrentPrice(newPrice);
    }

    /**
     * Nahodne urci zvysovanie/znizovanie ceny firmy na trhu.
     */
    private boolean isPriceIncreasing(){
        return randomConfig.random().nextInt(2) == 1;
    }

    /**
     * Vypocita novu cenu firmy.
     */
    private int calculateNewPrice(int price, int minPrice, int maxPrice) {
        int value = Math.max(1, Math.round(price / 100.0f));
        int newPrice = isPriceIncreasing() ? price + value : price - value;
        return Math.clamp(newPrice, minPrice, maxPrice);
    }
}
