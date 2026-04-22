package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Policko ktore je mozne kupit a vlastnit.
 * Hrac ktory stoji na tomto policku plati najom vlastnikovy podla poctu vlastnych policok.
 */
public class ServerTile extends AbstractOwnableTile{
    @Getter
    private final int price;
    private final int rent;

    public ServerTile(int position, String name, int price, int baseRent) {
        super(position, name);
        this.price = price;
        this.rent = baseRent;
    }

    /**
     * Definuje spravanie policka.
     * Policko nema vlastnika -> hrac policko moze kupit
     * Policko ma vlastnika -> hrac plati vlastnikovy podla toho kolko policok sam vlastni
     * vlastnikom je hrac ktory na policku stoji -> bez akcie
     */
    @Override
    public TileActionType interact(Game game, Player player) {
        if (getOwner() == null) {
            return new TileActionType(TileResult.WAIT_FOR_PURCHASE, price, rent, getOwner(), null);
        }

        if (getOwner() != player) {
            int propertyCount = countPlayerProperties(player, game);
            int totalRent = rent * propertyCount;

            return new TileActionType(TileResult.PAY_RENT, price, totalRent, getOwner(), null);
        }

        return new TileActionType(TileResult.NOTHING, price, rent, getOwner(), null);
    }

    /**
     * Spocita pocet policok ktore hrac vlastni.
     * Pouziva sa na vypocet poplatku.
     */
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
