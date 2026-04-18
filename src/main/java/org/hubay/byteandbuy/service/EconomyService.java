package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Buyable;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.hubay.byteandbuy.model.tiles.WorkshopTile;
import org.springframework.stereotype.Service;

// Riesi financne tranzakcie
@Service
public class EconomyService {
    // Metoda sa vykona ked sa hrac rozhodne kupit policko.
    // vykona kupu policka.
    public void buyProperty(Game game) {
        if (!game.isWaitingForDecision()) {
            throw new IllegalStateException("No decision expected");
        }

        Player player = game.getCurrentPlayer();
        Tile tile = game.getCurrentTile(player);

        if (!(tile instanceof Buyable buyable)) {
            throw new IllegalStateException("Tile is not buyable");
        }

        int price = calculateFinalPrice(game, player, buyable);

        if (player.canAfford(price)) {
            player.pay(price);
            buyable.setOwner(player);

            game.getEventCollector().add(player.getName() + " kúpil " + tile.getName() + " za " + price);
        } else {
            game.getEventCollector().add("Nemas dost na ucte");
        }

        game.resumePlaying();
    }

    // Vypocita finalnu sumu ktoru hrac zaplati pri nakupe noveho PropertyTile (zlacnene ked hrac vlastni dielnu).
    private int calculateFinalPrice(Game game, Player player, Buyable tile) {
        double discount = 0;

        for (Tile t : game.getBoard().getTiles()) {
            if (t instanceof WorkshopTile workshop) {
                if (workshop.getOwner() == player) {
                    discount += workshop.getDiscount();
                }
            }
        }

        return (int) (tile.getPrice() * (1 - discount));
    }
}
