package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Karticka obsahujuca posun hraca na konkretne policko.
public class MoveToPositionCard implements Card {
    // pozicia kam sa ma hrac posunut.
    private final int position;
    // Popis na karte.
    private final String description;

    public MoveToPositionCard(int position, String description) {
        this.position = position;
        this.description = description;
    }

    // Vykona presun hraca na danu poziciu na hracej doske.
    @Override
    public CardResult apply(Game game, Player player) {
        game.movePlayerTo(player, position, true);
        return CardResult.simple("Posun na " + description);
    }

    // Popis na karte.
    @Override
    public String getDescription() {
        return description;
    }
}
