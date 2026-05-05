package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.persistence.snapshot.CardSnapshot;

import java.util.UUID;

/**
 * Karta presunie hraca na dane policko na hracej doske.
 */
public class MoveToPositionCard implements Card {
    private final UUID id;
    private final int position;
    private final String description;
    private final boolean applyBonusStart;

    public MoveToPositionCard(int position, String description, boolean applyBonusStart) {
        this(UUID.randomUUID(), position, description, applyBonusStart);
    }

    public MoveToPositionCard(UUID id, int position, String description, boolean applyBonusStart) {
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
        return CardKind.MOVE_TO_POSITION;
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
