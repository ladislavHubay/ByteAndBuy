package org.hubay.byteandbuy.factory;

import org.hubay.byteandbuy.config.GameConfig;
import org.hubay.byteandbuy.model.board.Board;
import org.hubay.byteandbuy.model.cards.*;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameFactory {
    private final GameConfig config;

    private static final int START_POSITION = 0;
    private static final int JAIL_POSITION = 13;

    public GameFactory(GameConfig config) {
        this.config = config;
    }

    public Game createSampleGame() {
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

        return new Game(config, players, board, START_POSITION);
    }

    // Vytvori balicek kariet s nahodnymi efektami.
    private static Deck createCardsWithRandomEvents() {
        List<Card> randomEventCards = List.of(
                new MoveStepsCard(3, "Posun sa o 3 policka dopredu"),
                new MoveToPositionCard(START_POSITION, "Posun sa na START"),
                new GoToJailCard(JAIL_POSITION, "Presun sa do vazania")
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
    private List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Peter", START_POSITION, config.getStartMoney(), true));
        players.add(new Player("Katka", START_POSITION, config.getStartMoney(), true));
        players.add(new Player("Andrea", START_POSITION, config.getStartMoney(), true));

        return players;
    }

    // Vytvory zoznam hracich policok.
    private List<Tile> createTiles(Deck randomEventsDeck, Deck financialTransactionsDeck, Tile startTile) {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(startTile);

        PropertyGroup firma1 = new PropertyGroup("Firma 1");
        addPropertyGroup(tiles, firma1, 1, "policko_1", 100, 50);
        addPropertyGroup(tiles, firma1, 2, "policko_2", 100, 50);
        addPropertyGroup(tiles, firma1, 3, "policko_3", 100, 50);

        tiles.add(new WorkshopTile(4, "dielna_4", 100));

        tiles.add(new CardTile(5, "nahoda_5", randomEventsDeck));

        PropertyGroup firma2 = new PropertyGroup("Firma 2");
        addPropertyGroup(tiles, firma2, 6, "policko_6", 110, 70);
        addPropertyGroup(tiles, firma2, 7, "policko_7", 110, 70);
        addPropertyGroup(tiles, firma2, 8, "policko_8", 110, 70);

        tiles.add(new CardTile(9, "finance_9", financialTransactionsDeck));

        PropertyGroup firma3 = new PropertyGroup("Firma 3");
        addPropertyGroup(tiles, firma3, 10, "policko_10", 150, 80);
        addPropertyGroup(tiles, firma3, 11, "policko_11", 150, 80);

        tiles.add(new ServerTile(12, "Serverovna_12", 150, 20));
        tiles.add(new JailTile(13, "Vazanie_13"));

        tiles.add(new CardTile(14, "nahoda_14", randomEventsDeck));

        return tiles;
    }

    // Helper pre createTiles() - pre vytvaranie policok.
    private void addPropertyGroup(List<Tile> tiles, PropertyGroup propertyGroup,
                                         int position, String name, int price, int rent) {
        PropertyTile propertyTile = new PropertyTile(position, name, price, rent, propertyGroup, config.getFullGroupRentMultiplier());
        propertyGroup.addProperty(propertyTile);
        tiles.add(propertyTile);
    }
}
