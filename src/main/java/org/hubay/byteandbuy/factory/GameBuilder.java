package org.hubay.byteandbuy.factory;

import org.hubay.byteandbuy.config.GameConfig;
import org.hubay.byteandbuy.config.RandomConfig;
import org.hubay.byteandbuy.model.board.Board;
import org.hubay.byteandbuy.model.cards.*;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.persistence.snapshot.CardSnapshot;
import org.hubay.byteandbuy.persistence.snapshot.DeckSnapshot;
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
public class GameBuilder {
    private final RandomConfig random;
    private final GameConfig config;
    private final PlayerFactory playerFactory;
    private static final int START_POSITION = 0;
    private static final int JAIL_POSITION = 13;

    public GameBuilder(RandomConfig random, GameConfig config, PlayerFactory playerFactory) {
        this.random = random;
        this.config = config;
        this.playerFactory = playerFactory;
    }

    /**
     * Vytvori hru (Game).
     * Na vstupnych parametroch zavisi ci bude hra nova hra alebo existujuca - nacitana z DB.
     */
    public Game buildGame(List<Player> players, Deck cardDeck) {
        Companies companies = new Companies();
        Tile startTile = new StartTile(START_POSITION, "START");
        List<Tile> tiles = createTiles(cardDeck, startTile, companies);

        Board board = new Board(tiles, startTile);

        return new Game(config, players, board, START_POSITION, cardDeck, companies);
    }

    /**
     * Vytvori prazdnu hru bez hracov.
     */
    public Game createEmptyGame() {
        Deck cardDeck = createCardsWithRandomEvents();
        return buildGame(new ArrayList<>(), cardDeck);
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

        Deck cardDeck = restoreOrCreateCardDeck(snapshot.getCardDeck());
        List<Player> players = createPlayers(snapshot.getPlayers());

        return buildGame(players, cardDeck);
    }

    /**
     * Vytvori balicek kariet s nahodnymi udalostami.
     */
    private Deck createCardsWithRandomEvents() {
        List<Card> randomEventCards = List.of(
                new MoneyCard(50, "Vyhral si v loterii 50"),
                new MoneyCard(-50, "Zaplat pokutu 50"),
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
     * Obnovi balicek kariet s nahodnymi udalosti,
     * alebo vytvori novy ak snapshot este deck neobsahuje.
     */
    private Deck restoreOrCreateCardDeck(DeckSnapshot snapshot) {
        if (snapshot == null || snapshot.getCards() == null) {
            return createCardsWithRandomEvents();
        }

        return restoreDeck(snapshot);
    }

    /**
     * Obnovi poradie kariet a aktualnu poziciu v balicku.
     */
    private Deck restoreDeck(DeckSnapshot snapshot) {
        List<Card> cards = new ArrayList<>();

        for (CardSnapshot cardSnapshot : snapshot.getCards()) {
            cards.add(restoreCard(cardSnapshot));
        }

        return new Deck(cards, snapshot.getCurrentIndex());
    }

    /**
     * Obnovi kartu podla typu a parametrov ulozenych v snapshote.
     */
    private Card restoreCard(CardSnapshot snapshot) {
        return switch (snapshot.getKind()) {
            case MONEY -> new MoneyCard(
                    snapshot.getId(),
                    snapshot.getAmount(),
                    snapshot.getDescription()
            );
            case MOVE_STEPS -> new MoveStepsCard(
                    snapshot.getId(),
                    snapshot.getSteps(),
                    snapshot.getDescription(),
                    Boolean.TRUE.equals(snapshot.getApplyBonusStart())
            );
            case MOVE_TO_POSITION -> new MoveToPositionCard(
                    snapshot.getId(),
                    snapshot.getPosition(),
                    snapshot.getDescription(),
                    Boolean.TRUE.equals(snapshot.getApplyBonusStart())
            );
            case GO_TO_JAIL -> new GoToJailCard(
                    snapshot.getId(),
                    snapshot.getPosition(),
                    snapshot.getDescription(),
                    Boolean.TRUE.equals(snapshot.getApplyBonusStart())
            );
            case TELEPORT -> new TeleportCard(
                    snapshot.getId(),
                    snapshot.getDescription(),
                    Boolean.TRUE.equals(snapshot.getApplyBonusStart()),
                    random
            );
        };
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
    private List<Tile> createTiles(Deck cardDeck, Tile startTile, Companies companies) {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(startTile);

        PropertyGroup firma1 = new PropertyGroup("Firma 1", generateRandomGroupPrice(500));
        addPropertyGroup(tiles, firma1, 1, "policko_1");
        addPropertyGroup(tiles, firma1, 2, "policko_2");
        addPropertyGroup(tiles, firma1, 3, "policko_3");
        companies.addGroup(firma1);

        tiles.add(new WorkshopTile(4, "dielna_4", 500));

        tiles.add(new CardTile(5, "karty_5", cardDeck));

        PropertyGroup firma2 = new PropertyGroup("Firma 2", generateRandomGroupPrice(500));
        addPropertyGroup(tiles, firma2, 6, "policko_6");
        addPropertyGroup(tiles, firma2, 7, "policko_7");
        addPropertyGroup(tiles, firma2, 8, "policko_8");
        companies.addGroup(firma2);

        tiles.add(new CardTile(9, "karty_9", cardDeck));

        PropertyGroup firma3 = new PropertyGroup("Firma 3", generateRandomGroupPrice(500));
        addPropertyGroup(tiles, firma3, 10, "policko_10");
        addPropertyGroup(tiles, firma3, 11, "policko_11");
        companies.addGroup(firma3);

        tiles.add(new ServerTile(12, "Serverovna_12", 750, config.getRentForOnePropertyMultiplier()));
        tiles.add(new JailTile(13, "Vazanie_13"));

        tiles.add(new CardTile(14, "karty_14", cardDeck));

        return tiles;
    }

    /**
     * Vytvori skupiny a prida do nich policka ktore patria spolu.
     */
    private void addPropertyGroup(List<Tile> tiles, PropertyGroup propertyGroup,
                                         int position, String name) {
        PropertyTile propertyTile = new PropertyTile(
                position, name, propertyGroup, config.getFullGroupRentMultiplier(), config.getBaseRentRate()
        );
        propertyGroup.addProperty(propertyTile);
        tiles.add(propertyTile);
    }

    /**
     * Vygeneruje nahodnu cenu pre vsetky policka patriace do jednej skupiny.
     * Cena sa generuje v rozsahu urcenom v application.properties.
     */
    private int generateRandomGroupPrice(int averagePrice){
        return random.random().nextInt((int)(averagePrice * config.getInitialPriceMinMultiplier()),
                (int)(averagePrice * config.getInitialPriceMaxMultiplier()) + 1);
    }
}
