package org.hubay.byteandbuy.factory;

import org.hubay.byteandbuy.config.GameConfig;
import org.hubay.byteandbuy.model.board.Board;
import org.hubay.byteandbuy.model.cards.*;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.*;

import java.util.ArrayList;
import java.util.List;

public class GameFactory {
    private static final int START_MONEY = 210;
    private static final int START_BONUS = 10;
    private static final int START_POSITION = 0;

    public static Game createSampleGame() {
        // Hraci
        List<Player> players = createPlayers();

        // Balicky kariet
        Deck randomDeck = createCardsWithRandomEvents();
        Deck financeDeck = createCardsWithFinancialTransactions();

        // Tiles
        Tile startTile = new StartTile(START_POSITION, "START");
        List<Tile> tiles = createTiles(randomDeck, financeDeck, startTile);

        // Hracia doska
        Board board = new Board(tiles, startTile);

        // Pravidla hry
        GameConfig config = new GameConfig(START_BONUS);

        return new Game(config, players, board, START_POSITION);
    }

    // Vytvori balicek kariet s nahodnymi efektami.
    private static Deck createCardsWithRandomEvents() {
        List<Card> randomEventCards = List.of(
                new MoveStepsCard(1, "Posun sa o 1 policka dopredu"),
                new MoveToPositionCard(START_POSITION, "Posun sa na START"),
                new GoToJailCard(13, "Presun sa do vazania")
        );

        return new Deck(randomEventCards);
    }

    // Vytvori balicek kariet s financnymi efektami.
    private static Deck createCardsWithFinancialTransactions() {
        List<Card> financialTransactionCards = List.of(
                new MoneyCard(10, "Vyhral si v loterii 10"),
                new MoneyCard(-50, "Zaplat pokutu 50")
        );

        return new Deck(financialTransactionCards);
    }

    // Vytvori zoznam hracov.
    private static List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Peter", START_POSITION, START_MONEY, true));
        players.add(new Player("Katka", START_POSITION, START_MONEY, true));
        players.add(new Player("Andrea", START_POSITION, START_MONEY, true));

        return players;
    }

    // Vytvory zoznam hracich policok.
    private static List<Tile> createTiles(Deck randomEventsDeck, Deck financialTransactionsDeck, Tile startTile) {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(startTile);

        PropertyGroup firma1 = new PropertyGroup("Firma 1");
        addPropertyGroup(tiles, firma1, 1, "policko_1", 100, 50, null);
        addPropertyGroup(tiles, firma1, 2, "policko_2", 100, 50, null);
        addPropertyGroup(tiles, firma1, 3, "policko_3", 100, 50, null);

        tiles.add(new WorkshopTile(4, "dielna_4", 100, 0.2));

        tiles.add(new CardTile(5, "nahoda_5", randomEventsDeck));

        PropertyGroup firma2 = new PropertyGroup("Firma 2");
        addPropertyGroup(tiles, firma2, 6, "policko_6", 100, 50, null);
        addPropertyGroup(tiles, firma2, 7, "policko_7", 100, 50, null);
        addPropertyGroup(tiles, firma2, 8, "policko_8", 100, 50, null);

        tiles.add(new CardTile(9, "finance_9", financialTransactionsDeck));

        PropertyGroup firma3 = new PropertyGroup("Firma 3");
        addPropertyGroup(tiles, firma3, 10, "policko_10", 100, 50, null);
        addPropertyGroup(tiles, firma3, 11, "policko_11", 100, 50, null);

        tiles.add(new ServerTile(12, "Serverovna_12", 150, 20));
        tiles.add(new JailTile(13, "Vazanie_13"));

        tiles.add(new CardTile(14, "nahoda_14", randomEventsDeck));
        //tiles.add(new CardTile(15, "nahoda_15", randomEventsDeck));
        //tiles.add(new CardTile(16, "nahoda_16", randomEventsDeck));
        //tiles.add(new CardTile(17, "nahoda_17", randomEventsDeck));

        return tiles;
    }

    // Helper pre createTiles() - pre vytvaranie policok.
    private static void addPropertyGroup(List<Tile> tiles, PropertyGroup propertyGroup,
                                         int position, String name, int price, int rent, Player owner) {
        PropertyTile propertyTile = new PropertyTile(position, name, price, rent, owner, propertyGroup);
        propertyGroup.addProperty(propertyTile);
        tiles.add(propertyTile);
    }
}
