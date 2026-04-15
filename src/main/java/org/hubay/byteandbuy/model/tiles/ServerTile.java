package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import lombok.Setter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

public class ServerTile extends Tile implements Buyable, BankruptAware{
    // Cena za nakup policka
    @Getter
    private final int price;
    // Suma za platbu ak hrac zastavi na policku
    private final int rent;
    // Vlastnik policka
    @Setter
    @Getter
    private Player owner;

    public ServerTile(int position, String name, int price, int baseRent) {
        super(position, name);
        this.price = price;
        this.rent = baseRent;
    }

    // metoda implementuje spravanie konkretneho policka.
    @Override
    public TileResult interact(Game game, Player player) {
        if (owner == null) {
            String message = "Server je na predaj";
            System.out.println(message);

            return TileResult.decision(message);
        }

        if (owner == player) {
            String message = "Stojíš na vlastnom serveri";
            System.out.println(message);

            return TileResult.simple(message);
        }

        int propertyCount = countPlayerProperties(player, game);
        int totalRent = rent * propertyCount;

        player.pay(totalRent);
        owner.receive(totalRent);

        String message = player.getName() + " zaplatil " + totalRent + " za pouzitie serverovne hráčovi " + owner.getName();
        System.out.println(message);

        return TileResult.simple(message);
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

    @Override
    public void onPlayerBankrupt(Player player) {
        if (owner == player) {
            owner = null;
        }
    }
}
