package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.config.RandomConfig;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.persistence.snapshot.CardSnapshot;

import java.util.UUID;

/**
 * Karta presunie hraca na nahodne policko na hracej doske.
 */
public class TeleportCard implements Card {
    private final UUID id;
    private final String description;
    private final boolean applyBonusStart;
    private final RandomConfig random;

    public TeleportCard(String description, boolean applyBonusStart, RandomConfig random) {
        this(UUID.randomUUID(), description, applyBonusStart, random);
    }

    public TeleportCard(UUID id, String description, boolean applyBonusStart, RandomConfig random) {
        this.id = id;
        this.description = description;
        this.applyBonusStart = applyBonusStart;
        this.random = random;
    }

    /**
     * Vrati ID karty.
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Vrati tyo karty.
     */
    @Override
    public CardKind getKind() {
        return CardKind.TELEPORT;
    }

    /**
     * Presunie hraca na nahodnu poziciu na hracej doske.
     * Bonus za presun cez START alebo presun na START urcuje 'applyBonusStart'.
     */
    @Override
    public void apply(Game game, Player player) {
        game.movePlayerTo(player, random.random().nextInt(game.getBoardSize()), applyBonusStart);
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
        snapshot.setApplyBonusStart(applyBonusStart);
        return snapshot;
    }
}
