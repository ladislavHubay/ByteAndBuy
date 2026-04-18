package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

public class WorkshopTile extends AbstractOwnableTile{
    // Hodnota policka pre nakup
    @Getter
    private final int price;

    public WorkshopTile(int position, String name, int price) {
        super(position, name);
        this.price = price;
    }

    // metoda implementuje spravanie konkretneho policka.
    @Override
    public TileResult interact(Game game, Player player) {
        if (getOwner() == null) {
            game.getEventCollector().add("Môžeš kúpiť dielňu");

            return TileResult.WAIT_FOR_DECISION;
        }

        game.getEventCollector().add(player.getName() + " je na dielni – bez poplatku. Vlastni ho " + getOwner().getName());

        return TileResult.CONTINUE;
    }
}
