package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.player.Player;

/**
 * Abstraktna trieda pre vsetky druhy policka ktore je mozne v hre kupit.
 */
public abstract class AbstractOwnableTile extends Tile implements Buyable, BankruptAware{
    private Player owner;

    public AbstractOwnableTile(int position, String name) {
        super(position, name);
    }

    /**
     * Vrati aktualneho vlastnika policka.
     */
    @Override
    public Player getOwner() {
        return owner;
    }

    /**
     * Nastavi vlastnika policka.
     */
    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    /**
     * Ak hrac zkrachuje (alebo z ineho dovodu ukonci hru),
     * zrusi jeho vlastnictvo policka.
     * Policko tak mozne znovu kupit iny hrac.
     */
    @Override
    public void onPlayerBankrupt(Player player) {
        if (owner == player) {
            owner = null;
        }
    }
}
