package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Karta presunie hraca na dane policko na hracej doske.
 */
public class MoveToPositionCard implements Card {
    private final int position;
    private final String description;

    public MoveToPositionCard(int position, String description) {
        this.position = position;
        this.description = description;
    }

    /**
     * Presunie hraca na konkretnu poziciu na hracej doske.
     * Pri presune na START je aktivovany bonus za START.
     */
    @Override
    public void apply(Game game, Player player) {
        game.movePlayerTo(player, position, true);
    }

    /**
     * Textovy popis karty.
     */
    @Override
    public String getDescription() {
        return description;
    }
}
