package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.player.Player;

public interface Buyable {
    int getPrice();
    Player getOwner();
    void setOwner(Player player);
}
