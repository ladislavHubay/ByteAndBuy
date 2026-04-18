package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

public class ServerTile extends AbstractOwnableTile{
    // Cena za nakup policka
    @Getter
    private final int price;
    // Suma za platbu ak hrac zastavi na policku
    private final int rent;

    public ServerTile(int position, String name, int price, int baseRent) {
        super(position, name);
        this.price = price;
        this.rent = baseRent;
    }

    // metoda implementuje spravanie konkretneho policka.
    @Override
    public TileResult interact(Game game, Player player) {
        if (getOwner() == null) {
            game.getEventCollector().add("Server je na predaj");

            return TileResult.WAIT_FOR_DECISION;
        }

        if (getOwner() == player) {
            game.getEventCollector().add("Stojíš na vlastnom serveri");

            return TileResult.WAIT_FOR_DECISION;
        }

        int propertyCount = countPlayerProperties(player, game);
        int totalRent = rent * propertyCount;

        player.pay(totalRent);
        getOwner().receive(totalRent);

        game.getEventCollector().add(player.getName() + " zaplatil " + totalRent +
                " za pouzitie serverovne hráčovi " + getOwner().getName());

        return TileResult.CONTINUE;
    }

    // Metoda spocita pocet policok (propertyTile) ktore vlastni hrac.
    private int countPlayerProperties(Player player, Game game) {
        int count = 0;

        for (Tile tile : game.getBoard().getTiles()) {
            if (tile instanceof PropertyTile property) {
                if (property.getOwner() == player) {
                    count++;
                }
            }
        }

        return count;
    }
}
