package model.tiles;

import model.cards.Card;
import model.cards.Deck;
import model.game.Game;
import model.player.Player;

// Urcuje spravanie konkretneho policka - iba policka kde sa tahaju karty.
public class CardTile extends Tile{
    private final Deck deck;

    public CardTile(int position, String name, Deck deck) {
        super(position, name);
        this.deck = deck;
    }

    @Override
    public void interact(Game game, Player player) {
        Card card = deck.draw();

        System.out.println(player.getName() + " ťahá kartu: " + card.getDescription());

        card.apply(game, player);
    }






    // metoda implementuje spravanie pri tahani karty (zatial len simulacia tahania karty)
    //@Override
    //public void interact(Game game, Player player){
    //    System.out.println(player.getName() + " ťahá kartu: " + getName());
//
    //    int effect = new Random().nextInt(3);
//
    //    switch (effect) {
    //        case 0 -> {
    //            player.setMoney(player.getMoney() + 100);
    //            System.out.println("Získal si 100");
    //        }
    //        case 1 -> {
    //            player.setMoney(player.getMoney() - 50);
    //            System.out.println("Zaplatil si 50");
    //        }
    //        case 2 -> {
    //            player.setPosition(0);
    //            System.out.println("Presun na START");
    //        }
    //    }
    //}
}
