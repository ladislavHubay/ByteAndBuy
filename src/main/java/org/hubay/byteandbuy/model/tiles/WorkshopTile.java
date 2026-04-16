package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
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
            String message = "Môžeš kúpiť dielňu";
            System.out.println(message);

            return TileResult.decision(message);
        }

        String message = player.getName() + " je na dielni – bez poplatku. Vlastni ho " + getOwner().getName();
        System.out.println(message);

        return TileResult.simple(message);
    }
}
