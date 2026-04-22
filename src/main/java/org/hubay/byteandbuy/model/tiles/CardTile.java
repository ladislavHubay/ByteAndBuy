package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.cards.Deck;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Policko urcene na tahanie karty.
 */
public class CardTile extends Tile{
    private final Deck deck;

    public CardTile(int position, String name, Deck deck) {
        super(position, name);
        this.deck = deck;
    }

    /**
     * Hrac vytiahne kartu z balicka a vykona sa efekt na karte.
     * Moze menit stav hraca alebo hry.
     */
    @Override
    public TileActionType interact(Game game, Player player) {
        return new TileActionType(TileResult.DRAW_CARD, null, null, null, deck);
    }
}
