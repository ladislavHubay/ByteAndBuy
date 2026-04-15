package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// jedna karta (je jedno ci nahoda/finance)
public interface Card {
    // vykona udalost na karte.
    CardResult apply(Game game, Player player);

    // popis karty
    String getDescription();
}
