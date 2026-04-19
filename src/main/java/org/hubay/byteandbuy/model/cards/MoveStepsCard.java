package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Karta posuva hraca o urcity pocet policok dopredu.
 */
public class MoveStepsCard implements Card{
    private final int steps;
    private final String description;

    public MoveStepsCard(int steps, String description) {
        this.steps = steps;
        this.description = description;
    }

    /**
     * Posunie hraca o presny pocet krokov na hracej doske.
     * Pri prechode cez START je aktivavany bonus za START.
     */
    @Override
    public void apply(Game game, Player player) {
        game.movePlayer(player, steps, true);
    }

    /**
     * Textovy popis karty.
     */
    @Override
    public String getDescription() {
        return description;
    }
}
