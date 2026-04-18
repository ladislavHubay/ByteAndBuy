package org.hubay.byteandbuy.model.tiles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Urcuje spravanie konkretneho policka - iba policka s moznostou nakupu policka.
public class PropertyTile extends AbstractOwnableTile{
    // Skupina policok patriacich k sebe.
    @JsonBackReference
    private final PropertyGroup group;
    // cena pri moznosti kupi policka (ak ho nevlastni iny hrac).
    @Getter
    private final int price;
    // cena na zaplatenie najmu (ak toto policko vlastni iny hrac)
    private final int rent;

    public PropertyTile(int position, String name, int price, int rent, PropertyGroup group) {
        super(position, name);
        this.price = price;
        this.rent = rent;
        this.group = group;
    }

    @Override
    public TileResult interact(Game game, Player player) {
        if (getOwner() == null) {
            game.getEventCollector().add(player.getName() + " môže kúpiť " + getName());

            return TileResult.WAIT_FOR_DECISION;
        }

        if (getOwner() != player) {
            int payment = handleRentPayment(player);

            game.getEventCollector().add(player.getName() + " zaplatil nájom " + payment + " hracovy " + getOwner().getName());

            return TileResult.CONTINUE;
        }

        game.getEventCollector().add(player.getName() + " je na vlastnom políčku");

        return TileResult.CONTINUE;
    }

    // pomocna metoda na riesenie platby prenajmu ak jeden hrac stoji na policku vlastnenom inym hracom.
    private int handleRentPayment(Player player) {
        int payment = Math.min(player.getMoney(), calculateRent());

        player.pay(payment);
        getOwner().receive(payment);

        return payment;
    }

    // Navysi rent v pripade ak hrac vlastni vsetko z GROUP.
    private int calculateRent() {
        if (group != null && group.ownsAll(getOwner())) {
            return (int)(rent * 1.5);
        }
        return rent;
    }
}
