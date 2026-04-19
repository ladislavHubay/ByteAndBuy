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
    public TileResult interact(Game game, Player player) {
        if (getOwner() == null) {
            game.getEventCollector().add("Mozes kupit dielnu");
            return TileResult.WAIT_FOR_DECISION;
        }

        game.getEventCollector().add(player.getName() + " je na dielni. Vlastni ho " + getOwner().getName());
        return TileResult.CONTINUE;
    }
}
