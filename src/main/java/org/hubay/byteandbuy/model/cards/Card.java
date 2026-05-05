package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.persistence.snapshot.CardSnapshot;

import java.util.UUID;

/**
 * Reprezentuje kratu z balicka.
 */
public interface Card {
    /**
     * ID karty.
     */
    UUID getId();

    /**
     * Typ spravania karty.
     */
    CardKind getKind();

    /**
     * Vykona efekt karty.
     */
    void apply(Game game, Player player);

    /**
     * Textovy popis karty.
     */
    String getDescription();

    /**
     * Vytvori snapshot karty pre ulozenie balicka.
     */
    CardSnapshot toSnapshot();
}
