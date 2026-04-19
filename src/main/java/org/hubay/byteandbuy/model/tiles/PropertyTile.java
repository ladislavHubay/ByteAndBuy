package org.hubay.byteandbuy.model.tiles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Policko ktore je mozne vlastnit.
 */
public class PropertyTile extends AbstractOwnableTile{
    @JsonBackReference
    private final PropertyGroup group;
    @Getter
    private final int price;
    private final int rent;
    private final double fullGroupRentMultiplier;

    public PropertyTile(int position, String name, int price, int rent, PropertyGroup group, double fullGroupRentMultiplier) {
        super(position, name);
        this.price = price;
        this.rent = rent;
        this.group = group;
        this.fullGroupRentMultiplier = fullGroupRentMultiplier;
    }

    /**
     * Definuje spravanie policka.
     * ak nema vlastnika -> hrac moze kupit
     * ak je vlastnik iny hrac -> hrac plati najom
     * vlastnik je aktualny hrac -> ziadna akcia
     */
    @Override
    public TileResult interact(Game game, Player player) {
        if (getOwner() == null) {
            game.getEventCollector().add(player.getName() + " moze kupit " + getName());
            return TileResult.WAIT_FOR_DECISION;
        }

        if (getOwner() != player) {
            int payment = handleRentPayment(player);

            game.getEventCollector().add(player.getName() + " zaplatil majom: " + payment + " hracovy " + getOwner().getName());
            return TileResult.CONTINUE;
        }

        game.getEventCollector().add(player.getName() + " je na vlastnom policku");
        return TileResult.CONTINUE;
    }

    /**
     * Vykona platbu za najom.
     * V pripade nedostatku na ucte hrac vlastnikovy plati len do vysky co na ucte ma.
     */
    private int handleRentPayment(Player player) {
        int payment = Math.min(player.getMoney(), calculateRent());

        player.pay(payment);
        getOwner().receive(payment);

        return payment;
    }

    /**
     * Vypocita vysku najmu.
     * Ak hrac vlastni vsetky policka zo skupiny, najom je vyssi.
     */
    private int calculateRent() {
        if (group != null && group.ownsAll(getOwner())) {
            return (int)(rent * fullGroupRentMultiplier);
        }
        return rent;
    }
}
