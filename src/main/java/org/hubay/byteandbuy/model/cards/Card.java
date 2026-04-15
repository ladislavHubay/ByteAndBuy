package model.cards;

import model.game.Game;
import model.player.Player;

// jedna karta (je jedno ci nahoda/finance)
public interface Card {
    void apply(Game game, Player player);

    // popis karty
    String getDescription(); // optional – pre debug / UI
}
