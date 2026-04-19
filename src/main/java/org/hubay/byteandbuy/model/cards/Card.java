package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Reprezentuje kratu z balicka.
 */
public interface Card {
    /**
     * Vykona efekt karty.
     */
    void apply(Game game, Player player);
    /**
     * Textovy popis karty.
     */
    String getDescription();
}
