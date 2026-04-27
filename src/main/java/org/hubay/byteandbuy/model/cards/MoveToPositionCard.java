package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Karta presunie hraca na dane policko na hracej doske.
 */
public class MoveToPositionCard implements Card {
    private final int position;
    private final String description;
    private final boolean applyBonusStart;

    public MoveToPositionCard(int position, String description, boolean applyBonusStart) {
        this.position = position;
        this.description = description;
        this.applyBonusStart = applyBonusStart;
    }

    /**
     * Presunie hraca na konkretnu poziciu na hracej doske.
     * Bonus za presun cez START alebo presun na START urcuje 'applyBonusStart'.
     */
    @Override
    public void apply(Game game, Player player) {
        game.movePlayerTo(player, position, applyBonusStart);
    }

    /**
     * Textovy popis karty.
     */
    @Override
    public String getDescription() {
        return description;
    }
}
