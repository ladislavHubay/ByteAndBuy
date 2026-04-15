package model.tiles;

import model.game.Game;
import model.player.Player;

// Urcuje spravanie konkretneho policka - iba policka s moznostou nakupu policka.
public class PropertyTile extends Tile {
    // cena pri moznosti kupi policka (ak ho nevlastni iny hrac).
    private final int price;
    // cena na zaplatenie najmu (ak toto policko vlastni iny hrac)
    private final int rent;
    // Hrac ktory policko vlastni.
    private Player owner;

    public PropertyTile(int position, String name, int price, int rent, Player owner) {
        super(position, name);
        this.price = price;
        this.rent = rent;
        this.owner = owner;
    }

    // metoda implementuje spravanie konkretneho policka.
    @Override
    public void interact(Game game, Player player) {
        if(owner == null){
            handleUnownedProperty(player);
            buy(player);
        } else if (owner != player) {
            handleRentPayment(player);
        }
    }

    // kontrolny vypis
    private void handleUnownedProperty(Player player) {
        System.out.println(player.getName() + " môže kúpiť " + getName());
    }

    // pomocna metoda na riesenie platby prenajmu ak jeden hrac stoji na policku vlastnenom inym hracom.
    private void handleRentPayment(Player player) {
        int payment = Math.min(player.getMoney(), rent);

        player.setMoney(player.getMoney() - payment);
        owner.setMoney(owner.getMoney() + payment);
        System.out.println(player.getName() + " zaplatil " + payment + " " + owner.getName() + "-ovi");
    }

    // metoda implementuje spravanie pri nakupe policka aktualnym hracom.
    private void buy(Player player) {
        System.out.println("Kontrola: owner: " + owner + " || cena: " + price);
        if (player.getMoney() >= price) {
            player.setMoney(player.getMoney() - price);
            owner = player;
            System.out.println(player.getName() + " kúpil " + getName());
        } else {
            System.out.println(player.getName() + " nema dostatok na ucte " + player.getMoney());
        }
    }

    // metoda v pripade bankrotu hraca nastavy jeho kupene policko na null - znovu kupitelne.
    @Override
    public void onPlayerBankrupt(Player player) {
        if (owner == player) {
            owner = null;
        }
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
