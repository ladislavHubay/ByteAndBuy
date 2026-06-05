package org.hubay.byteandbuy.model.tiles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Policko ktore je mozne vlastnit.
 */
public class PropertyTile extends AbstractOwnableTile{
    @JsonBackReference
    private final PropertyGroup group;
    private final double fullGroupRentMultiplier;
    private final double baseRentRate;

    public PropertyTile(int position, String name, PropertyGroup group, double fullGroupRentMultiplier, double baseRentRate) {
        super(position, name, TileType.PROPERTY);
        this.group = group;
        this.fullGroupRentMultiplier = fullGroupRentMultiplier;
        this.baseRentRate = baseRentRate;
    }

    /**
     * Definuje spravanie policka.
     * ak nema vlastnika -> hrac moze kupit
     * ak je vlastnik iny hrac -> hrac plati najom
     * vlastnik je aktualny hrac -> ziadna akcia
     */
    @Override
    public TileActionType interact(Game game, Player player) {
        int payment = getRent();

        if (getOwner() == null) {
            return new TileActionType(TileResult.WAIT_FOR_PURCHASE, getPrice(), payment, getOwner(), null);
        }

        if (getOwner() != player) {
            payment = handleRentPayment(player);
            return new TileActionType(TileResult.PAY_RENT, getPrice(), payment, getOwner(), null);
        }

        return new TileActionType(TileResult.NOTHING, getPrice(), payment, getOwner(), null);
    }

    /**
     * Vykona platbu za najom.
     * V pripade nedostatku na ucte hrac vlastnikovy plati len do vysky co na ucte ma.
     */
    private int handleRentPayment(Player player) {
        return Math.min(player.getMoney(), getRent());
    }

    /**
     * Vypocita vysku najmu.
     * Ak hrac vlastni vsetky policka zo skupiny, najom je vyssi.
     */
    public int getRent() {
        if (getOwner() != null && group != null && group.ownsAll(getOwner())) {
            return (int)(calculateRent() * fullGroupRentMultiplier);
        }
        return calculateRent();
    }

    /**
     * Vrati aktualnu cenu policka.
     */
    public int getPrice(){
        return group.getCurrentPrice();
    }

    /**
     * Vypocita zakladny najom podla aktualnej ceny policka.
     */
    private int calculateRent() {
        return (int) (getPrice() * baseRentRate);
    }
}
