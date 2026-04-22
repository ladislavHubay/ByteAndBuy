package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

public class WorkshopTile extends AbstractOwnableTile{
    @Getter
    private final int price;

    /**
     * Policko je mozne kupit.
     * Vlastnikovy dava vyhodu (zlavu) pri nakupe dalsich policok.
     */
    public WorkshopTile(int position, String name, int price) {
        super(position, name);
        this.price = price;
    }

    /**
     * Definuje spravanie policka.
     * Majitel policka ziskava vyhodu (zlavu) pri nakupe dalsich policok.
     */
    @Override
    public TileActionType interact(Game game, Player player) {
        if (getOwner() == null) {
            return new TileActionType(TileResult.WAIT_FOR_PURCHASE, price, null, getOwner(), null);
        }

        return new TileActionType(TileResult.NOTHING, price, null, getOwner(), null);
    }
}
