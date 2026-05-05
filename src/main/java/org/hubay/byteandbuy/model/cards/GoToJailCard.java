package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.persistence.snapshot.CardSnapshot;

import java.util.UUID;

/**
 * Karta, ktora presunie hraca do vazania.
 */
public class GoToJailCard implements Card {
    private final UUID id;
    private final int position;
    private final String description;
    private final boolean applyBonusStart;

    public GoToJailCard(int position, String description, boolean applyBonusStart) {
        this(UUID.randomUUID(), position, description, applyBonusStart);
    }

    public GoToJailCard(UUID id, int position, String description, boolean applyBonusStart) {
        this.id = id;
        this.position = position;
        this.description = description;
        this.applyBonusStart = applyBonusStart;
    }

    /**
     * Vrati ID karty.
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Vrati typ karty.
     */
    @Override
    public CardKind getKind() {
        return CardKind.GO_TO_JAIL;
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

    /**
     * Vytrvori snapshot karty pre ulozenie do balicka.
     */
    @Override
    public CardSnapshot toSnapshot() {
        CardSnapshot snapshot = new CardSnapshot(id, getKind(), description);
        snapshot.setPosition(position);
        snapshot.setApplyBonusStart(applyBonusStart);
        return snapshot;
    }
}
