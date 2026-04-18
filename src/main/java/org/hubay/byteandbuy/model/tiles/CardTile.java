package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.cards.Card;
import org.hubay.byteandbuy.model.cards.Deck;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Urcuje spravanie konkretneho policka - iba policka kde sa tahaju karty.
public class CardTile extends Tile{
    private final Deck deck;

    public CardTile(int position, String name, Deck deck) {
        super(position, name);
        this.deck = deck;
    }

    // implementuje spravanie policka ked na nom stoji hrac.
    @Override
    public TileResult interact(Game game, Player player) {
        Card card = deck.draw();

        game.getEventCollector().add(player.getName() + " ťahá kartu: " + card.getDescription());

        card.apply(game, player);

        return TileResult.CONTINUE;
    }
}
