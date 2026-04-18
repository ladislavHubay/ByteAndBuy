package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import org.hubay.byteandbuy.dto.TurnResponse;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

public class WorkshopTile extends AbstractOwnableTile{
    // Hodnota policka pre nakup
    @Getter
    private final int price;
    // Hodnota o kolko sa znizi cena dalsieho PropertyTile pre hraca. Napr. 0.1 = 10%
    @Getter
    private final double discount;

    public WorkshopTile(int position, String name, int price, double discount) {
        super(position, name);
        this.price = price;
        this.discount = discount;
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
