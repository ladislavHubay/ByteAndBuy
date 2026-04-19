package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.player.Player;

/**
 * Rozgranie pre vsetky policka ktore sa v hre daju kupit.
 */
public interface Buyable {
    /**
     * Vrati cenu policka.
     */
    int getPrice();

    /**
     * Vrati vlastnika policka.
     */
    Player getOwner();

    /**
     * Nastavy vlastnika policka.
     */
    void setOwner(Player player);
}
