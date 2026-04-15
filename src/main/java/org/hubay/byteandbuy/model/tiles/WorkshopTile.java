package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import lombok.Setter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

public class WorkshopTile extends Tile implements Buyable, BankruptAware{
    // Hodnota policka pre nakup
    @Getter
    private final int price;
    // Hodnota o kolko sa znizi cena dalsieho PropertyTile pre hraca. Napr. 0.1 = 10%
    @Getter
    private final double discount;
    // Majitel policka.
    @Setter
    @Getter
    private Player owner;

    public WorkshopTile(int position, String name, int price, double discount) {
        super(position, name);
        this.price = price;
        this.discount = discount;
    }

    // metoda implementuje spravanie konkretneho policka.
    @Override
    public TileResult interact(Game game, Player player) {
        if (owner == null) {
            String message = "Môžeš kúpiť dielňu";
            System.out.println(message);

            return TileResult.decision(message);
        }

        String message = player.getName() + " je na dielni – bez poplatku. Vlastni ho " + owner.getName();
        System.out.println(message);

        return TileResult.simple(message);
    }

    @Override
    public void onPlayerBankrupt(Player player) {
        if (owner == player) {
            owner = null;
        }
    }
}
