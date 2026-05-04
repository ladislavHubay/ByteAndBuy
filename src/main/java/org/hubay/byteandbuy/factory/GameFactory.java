package org.hubay.byteandbuy.factory;

import org.hubay.byteandbuy.config.GameConfig;
import org.hubay.byteandbuy.config.RandomConfig;
import org.hubay.byteandbuy.model.board.Board;
import org.hubay.byteandbuy.model.cards.*;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.persistence.snapshot.GameSnapshot;
import org.hubay.byteandbuy.persistence.snapshot.PlayerSnapshot;
import org.hubay.byteandbuy.model.tiles.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Inicializuje hracov, hernu dosku, balicky kariet - vytvory celu hru.
 */
@Component
public class GameFactory {
    private final RandomConfig random;
    private final GameConfig config;
    private final PlayerFactory playerFactory;
    private static final int START_POSITION = 0;
    private static final int JAIL_POSITION = 13;

    public GameFactory(RandomConfig random, GameConfig config, PlayerFactory playerFactory) {
        this.random = random;
        this.config = config;
        this.playerFactory = playerFactory;
    }

    /**
     * Vytvori prazdnu hru bez hracov.
     */
    public Game createEmptyGame() {
        return createGame(new ArrayList<>());
    }

    /**
     * Vytvori noveho hraca s default nastavenim.
     */
    public Player createPlayer(String name) {
        return playerFactory.createNew(name, START_POSITION, config.getStartMoney(), true);
    }

    /**
     * Vytvori hru.
     * Ak hra uz bola ulozena v DB tak z nej vytvory hru,
     * ak nie tak vytvori uplne novu prazdnu hru.
     */
    public Game createGame(GameSnapshot snapshot) {
        if (snapshot == null || snapshot.getPlayers() == null) {
            return createEmptyGame();
        }

        return createGame(createPlayers(snapshot.getPlayers()));
    }

    /**
     * Vytvori hru z DB (snapshotu).
     */
    private Game createGame(List<Player> players) {
        Deck randomDeck = createCardsWithRandomEvents();
        Deck financeDeck = createCardsWithFinancialTransactions();

        Tile startTile = new StartTile(START_POSITION, "START");
        List<Tile> tiles = createTiles(randomDeck, financeDeck, startTile);

        Board board = new Board(tiles, startTile);

        return new Game(config, players, board, START_POSITION);
    }

    /**
     * Vytvori balicek kariet (karty s posunom pozicii hraca).
     */
    private Deck createCardsWithRandomEvents() {
        List<Card> randomEventCards = List.of(
                new MoveStepsCard(3, "Posun sa o 3 policka dopredu", true),
                new MoveToPositionCard(START_POSITION, "Posun sa na START", true),
                new GoToJailCard(JAIL_POSITION, "Presun sa do vazania", false),
                new TeleportCard(
                        "Oslavy firemných úspechov sa vymkli spod kontroly. Rano si sa zobudil na neocakavanom mieste.",
                        false,
                        random
                )
        );

        return new Deck(randomEventCards);
    }

    /**
     * Vytvori balicek kariet (karty s transakciami na ucte).
     */
    private Deck createCardsWithFinancialTransactions() {
        List<Card> financialTransactionCards = List.of(
                new MoneyCard(50, "Vyhral si v loterii 50"),
                new MoneyCard(-50, "Zaplat pokutu 50")
        );

        return new Deck(financialTransactionCards);
    }

    /**
     * Obnovi hracov z DB (zo snapshotu).
     */
    private List<Player> createPlayers(List<PlayerSnapshot> snapshots) {
        List<Player> players = new ArrayList<>();

        for (PlayerSnapshot snapshot : snapshots) {
            players.add(playerFactory.restore(
                    snapshot.getId(),
                    snapshot.getName(),
                    snapshot.getPosition(),
                    snapshot.getMoney(),
                    snapshot.isInGame(),
                    snapshot.isInJail()
            ));
        }

        return players;
    }

    /**
     * Vytvori policka na hracej doske.
     */
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

    /**
     * Vytvori skupiny a prida do nich policka ktore patria spolu.
     */
    private void addPropertyGroup(List<Tile> tiles, PropertyGroup propertyGroup,
                                         int position, String name, int price, int rent) {
        PropertyTile propertyTile = new PropertyTile(position, name, price, rent, propertyGroup, config.getFullGroupRentMultiplier());
        propertyGroup.addProperty(propertyTile);
        tiles.add(propertyTile);
    }
}
