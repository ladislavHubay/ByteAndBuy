package factory;

import model.board.Board;
import model.cards.*;
import model.game.Game;
import model.player.Player;
import model.tiles.CardTile;
import model.tiles.PropertyTile;
import model.tiles.StartTile;
import model.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class GameFactory {
    public static Game createSampleGame() {
        // Hraci
        List<Player> players = createPlayers();

        // Balicky kariet
        Deck randomDeck = createCardsWithRandomEvents();
        Deck financeDeck = createCardsWithFinancialTransactions();

        // Tiles
        Tile startTile = new StartTile(0, "START", 10);
        List<Tile> tiles = createTiles(randomDeck, financeDeck, startTile);

        // Hracia doska
        Board board = new Board(tiles, startTile);

        return new Game(players, board, 0, false);
    }

    // Vytvori balicek kariet s nahodnymi efektami.
    private static Deck createCardsWithRandomEvents() {
        List<Card> randomEventCards = List.of(
                new MoveStepsCard(3, "Posun sa o 3 policka dopredu"),
                new MoveToPositionCard(0, "Posun sa na START")
        );

        return new Deck(randomEventCards);
    }

    // Vytvori balicek kariet s financnymi efektami.
    private static Deck createCardsWithFinancialTransactions() {
        List<Card> financialTransactionCards = List.of(
                new MoneyCard(100, "Vyhral si v loterii 100"),
                new MoneyCard(-50, "Zaplat pokutu 50")
        );

        return new Deck(financialTransactionCards);
    }

    // Vytvori zoznam hracov.
    private static List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Peter", 0, 1000, true));
        players.add(new Player("Katka", 0, 1000, true));
        players.add(new Player("Andrea", 0, 1000, true));

        return players;
    }

    // Vytvory zoznam hracich policok.
    private static List<Tile> createTiles(Deck randomEventsDeck, Deck FinancialTransactionsDeck, Tile startTile) {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(startTile);
        tiles.add(new PropertyTile(1, "policko_1", 100, 5, null));
        tiles.add(new PropertyTile(2, "policko_2", 150, 10, null));
        tiles.add(new PropertyTile(3, "policko_3", 200, 15, null));
        tiles.add(new CardTile(4, "nahoda_4", randomEventsDeck));
        tiles.add(new PropertyTile(5, "policko_5", 300, 25, null));
        tiles.add(new PropertyTile(6, "policko_6", 350, 30, null));
        tiles.add(new PropertyTile(7, "policko_7", 400, 35, null));
        tiles.add(new CardTile(8, "finance_8", FinancialTransactionsDeck));
        tiles.add(new PropertyTile(9, "policko_9", 500, 45, null));
        tiles.add(new PropertyTile(10, "policko_10", 550, 50, null));

        return tiles;
    }
}
