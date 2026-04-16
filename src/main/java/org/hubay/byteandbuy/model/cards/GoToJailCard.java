package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Karta presun do vazania
public class GoToJailCard implements Card {
    // pozicia vazania
    private final int position;
    // popis karty
    private final String description;

    public GoToJailCard(int position, String description) {
        this.position = position;
        this.description = description;
    }

    @Override
    public CardResult apply(Game game, Player player) {
        game.movePlayerTo(player, position, false);
        player.setInJail(true);

        return CardResult.simple(description);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
