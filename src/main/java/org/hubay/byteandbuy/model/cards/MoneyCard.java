package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Karticka obsahujuca financna akciu.
public class MoneyCard implements Card {
    // Hodnota na karte
    private final int money;
    // Popis karty
    private final String description;

    public MoneyCard(int money, String description) {
        this.money = money;
        this.description = description;
    }

    // Metoda vykona financnu transakciu na ucte hraca podla karty.
    @Override
    public void apply(Game game, Player player) {
        player.receive(money);
    }

    // Vrati popis karty.
    @Override
    public String getDescription() {
        return description;
    }
}
