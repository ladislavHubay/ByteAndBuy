package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Karta, ktora presunie hraca do vazania.
 */
public class GoToJailCard implements Card {
    private final int position;
    private final String description;
    private final boolean applyBonusStart;

    public GoToJailCard(int position, String description, boolean applyBonusStart) {
        this.position = position;
        this.description = description;
        this.applyBonusStart = applyBonusStart;
    }

    /**
     * Presunie hraca do vazania a jeho stav nastavy na 'vo vazani' (inJail = true).
     * Bonus za presun cez START urcuje 'applyBonusStart'.
     */
    @Override
    public void apply(Game game, Player player) {
        game.movePlayerTo(player, position, applyBonusStart);
        player.setInJail(true);
    }

    /**
     * Textovy popis karty.
     */
    @Override
    public String getDescription() {
        return description;
    }
}
