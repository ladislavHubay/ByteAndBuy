package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import org.hubay.byteandbuy.model.cards.Deck;
import org.hubay.byteandbuy.model.player.Player;

@Getter
public class TileActionType {
    private final TileResult type;
    private final Integer price;
    private final Integer rent;
    private final Player owner;
    private final Deck deck;

    public TileActionType(TileResult type, Integer price, Integer rent, Player owner, Deck deck) {
        this.type = type;
        this.price = price;
        this.rent = rent;
        this.owner = owner;
        this.deck = deck;
    }
}
