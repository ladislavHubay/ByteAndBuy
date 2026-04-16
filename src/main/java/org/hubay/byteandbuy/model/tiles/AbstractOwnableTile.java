package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.player.Player;

public abstract class AbstractOwnableTile extends Tile implements Buyable, BankruptAware{
    private Player owner;

    public AbstractOwnableTile(int position, String name) {
        super(position, name);
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    @Override
    public void onPlayerBankrupt(Player player) {
        if (owner == player) {
            owner = null;
        }
    }
}
