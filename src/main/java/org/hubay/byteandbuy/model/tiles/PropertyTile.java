package org.hubay.byteandbuy.model.tiles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Urcuje spravanie konkretneho policka - iba policka s moznostou nakupu policka.
public class PropertyTile extends Tile implements BankruptAware, Buyable{
    // Skupina policok patriacich k sebe.
    @Getter
    @Setter
    @JsonBackReference
    private PropertyGroup group;
    // cena pri moznosti kupi policka (ak ho nevlastni iny hrac).
    @Getter
    private final int price;
    // cena na zaplatenie najmu (ak toto policko vlastni iny hrac)
    private final int rent;
    // Hrac ktory policko vlastni.
    @Setter
    @Getter
    private Player owner;

    public PropertyTile(int position, String name, int price, int rent, Player owner, PropertyGroup group) {
        super(position, name);
        this.price = price;
        this.rent = rent;
        this.owner = owner;
        this.group = group;
    }

    @Override
    public TileResult interact(Game game, Player player) {
        if (owner == null) {
            String message = player.getName() + " môže kúpiť " + getName();
            System.out.println(message);

            return TileResult.decision(message);
        }

        if (owner != player) {
            int payment = handleRentPayment(player);

            String message = player.getName() + " zaplatil nájom " + payment + " hracovy " + owner.getName();
            System.out.println(message);

            return TileResult.simple(message);
        }

        return TileResult.simple(player.getName() + " je na vlastnom políčku");
    }

    // metoda v pripade bankrotu hraca nastavy jeho kupene policko na null - znovu kupitelne.
    @Override
    public void onPlayerBankrupt(Player player) {
        if (owner == player) {
            owner = null;
        }
    }

    // pomocna metoda na riesenie platby prenajmu ak jeden hrac stoji na policku vlastnenom inym hracom.
    private int handleRentPayment(Player player) {
        int payment = Math.min(player.getMoney(), calculateRent());

        player.pay(payment);
        owner.receive(payment);

        return payment;
    }

    // Navysi rent v pripade ak hrac vlastni vsetko z GROUP.
    private int calculateRent() {
        if (group != null && group.ownsAll(owner)) {
            return (int)(rent * 1.5);
        }
        return rent;
    }
}
