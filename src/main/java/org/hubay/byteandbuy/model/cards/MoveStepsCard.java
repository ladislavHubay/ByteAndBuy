package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.persistence.snapshot.CardSnapshot;

import java.util.UUID;

/**
 * Karta posuva hraca o urcity pocet policok dopredu.
 */
public class MoveStepsCard implements Card{
    private final UUID id;
    private final int steps;
    private final String description;
    private final boolean applyBonusStart;

    public MoveStepsCard(int steps, String description, boolean applyBonusStart) {
        this(UUID.randomUUID(), steps, description, applyBonusStart);
    }

    public MoveStepsCard(UUID id, int steps, String description, boolean applyBonusStart) {
        this.id = id;
        this.steps = steps;
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
        return CardKind.MOVE_STEPS;
    }

    /**
     * Posunie hraca o presny pocet krokov na hracej doske.
     * Bonus za presun cez START alebo presun na START urcuje 'applyBonusStart'.
     */
    @Override
    public void apply(Game game, Player player) {
        game.movePlayer(player, steps, applyBonusStart);
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
        snapshot.setSteps(steps);
        snapshot.setApplyBonusStart(applyBonusStart);
        return snapshot;
    }
}
