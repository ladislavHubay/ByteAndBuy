package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.persistence.snapshot.CardSnapshot;

import java.util.UUID;

/**
 * Karta sposobuje fifnancu zmenu pre hraca.
 * Moze sposobit financnu stratu alebo aj zisk.
 */
public class MoneyCard implements Card {
    private final UUID id;
    private final int money;
    private final String description;

    public MoneyCard(int money, String description) {
        this(UUID.randomUUID(), money, description);
    }

    public MoneyCard(UUID id, int money, String description) {
        this.id = id;
        this.money = money;
        this.description = description;
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
        return CardKind.MONEY;
    }

    /**
     * Vykona financu transakciu hraca.
     * Transakcia moze byt zisk alebo strata.
     */
    @Override
    public void apply(Game game, Player player) {
        player.receive(money);
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
        snapshot.setAmount(money);
        return snapshot;
    }
}
