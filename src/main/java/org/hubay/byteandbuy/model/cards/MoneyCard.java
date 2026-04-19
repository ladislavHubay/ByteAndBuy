package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Karta sposobuje fifnancu zmenu pre hraca.
 * Moze sposobit financnu stratu alebo aj zisk.
 */
public class MoneyCard implements Card {
    private final int money;
    private final String description;

    public MoneyCard(int money, String description) {
        this.money = money;
        this.description = description;
    }

    /**
     * Vykona financu transakciu hraca.
     * Transakcia moze byt zisk alebo strata.
     */
    @Override
    public void apply(Game game, Player player) {
        player.receive(money);
    }

    /**
     * Textovy popis karty.
     */
    @Override
    public String getDescription() {
        return description;
    }
}
