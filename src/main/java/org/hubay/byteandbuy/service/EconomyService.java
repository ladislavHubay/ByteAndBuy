package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Buyable;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.hubay.byteandbuy.model.tiles.WorkshopTile;
import org.springframework.stereotype.Service;

/**
 * Trieda zodpoveda za financne operacie v hre.
 */
@Service
public class EconomyService {
    /**
     * Vykona nakup policka na ktorom hrac stoji
     * ak su splnene vsetky podmienky.
     */
    public void buyProperty(Game game, Player player) {
        if (!game.isWaitingForDecision()) {
            throw new IllegalStateException("No decision expected");
        }

        Tile tile = game.getCurrentTile(player);

        if (!(tile instanceof Buyable buyable)) {
            throw new IllegalStateException("Tile is not buyable");
        }

        int price = calculateFinalPrice(game, player, buyable);

        if (player.canAfford(price)) {
            player.pay(price);
            buyable.setOwner(player);

            game.getEventCollector().add(player.getName() + " kupil " + tile.getName() + " za " + price);
        } else {
            game.getEventCollector().add("Nemas dost na ucte");
        }

        game.resumePlaying();
    }

    /**
     * Vypocita finalnu cenu policka podla aktualnych bonusov pre hraca.
     * (Napr. ak vlastni 'dielnu' ziskava zlavu definovanu v application.properties)
     */
    private int calculateFinalPrice(Game game, Player player, Buyable tile) {
        double discount = 0;
        for (Tile t : game.getBoard().getTiles()) {
            if (t instanceof WorkshopTile workshop) {
                if (workshop.getOwner() == player) {
                    discount += game.getGameConfig().getWorkshopDiscount();
                }
            }
        }

        return (int) (tile.getPrice() * (1 - discount));
    }
}
