package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.config.RandomConfig;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Karta presunie hraca na nahodne policko na hracej doske.
 */
public class TeleportCard implements Card {
    private final String description;
    private final boolean applyBonusStart;
    private final RandomConfig random;

    public TeleportCard(String description, boolean applyBonusStart, RandomConfig random) {
        this.description = description;
        this.applyBonusStart = applyBonusStart;
        this.random = random;
    }

    /**
     * Presunie hraca na nahodnu poziciu na hracej doske.
     * Bonus za presun cez START alebo presun na START urcuje 'applyBonusStart'.
     */
    @Override
    public void apply(Game game, Player player) {
        game.movePlayerTo(player, random.random().nextInt(game.getBoardSize()), applyBonusStart);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
