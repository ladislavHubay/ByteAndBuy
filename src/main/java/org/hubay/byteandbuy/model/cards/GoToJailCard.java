package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Karta, ktora presunie hraca do vazania.
 */
public class GoToJailCard implements Card {
    private final int position;
    private final String description;

    public GoToJailCard(int position, String description) {
        this.position = position;
        this.description = description;
    }

    /**
     * Presunie hraca do vazania a jeho stav nastavy na 'vo vazani' (inJail = true).
     * Deaktivuje narok na bonus pri prechode cez policko START.
     */
    @Override
    public void apply(Game game, Player player) {
        game.movePlayerTo(player, position, false);
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
