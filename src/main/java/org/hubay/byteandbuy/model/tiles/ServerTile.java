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
    private final double rentForOnePropertyMultiplier;

    public ServerTile(int position, String name, int price, double rentForOnePropertyMultiplier) {
        super(position, name, TileType.SERVER);
        this.price = price;
        this.rentForOnePropertyMultiplier = rentForOnePropertyMultiplier;
    }

    /**
     * Definuje spravanie policka.
     * Policko nema vlastnika -> hrac policko moze kupit
     * Policko ma vlastnika -> hrac plati vlastnikovy podla toho kolko policok sam vlastni
     * vlastnikom je hrac ktory na policku stoji -> bez akcie
     */
    @Override
    public TileActionType interact(Game game, Player player) {
        int rent = Math.min(player.getMoney(), countPlayerProperties(player, game));

        if (getOwner() == null) {
            return new TileActionType(TileResult.WAIT_FOR_PURCHASE, price, rent, getOwner(), null);
        }

        if (getOwner() != player) {
            return new TileActionType(TileResult.PAY_RENT, price, rent, getOwner(), null);
        }

        return new TileActionType(TileResult.NOTHING, price, rent, getOwner(), null);
    }

    /**
     * Vypocita vysku poplatku.
     */
    private Integer countPlayerProperties(Player player, Game game) {
        double totalRent = 0;

        for (Tile tile : game.getBoard().getTiles()) {
            if (tile instanceof PropertyTile property) {
                if (property.getOwner() == player) {
                    totalRent = totalRent + (property.getPrice() * rentForOnePropertyMultiplier);
                }
            }
        }

        return (int) totalRent;
    }
}
