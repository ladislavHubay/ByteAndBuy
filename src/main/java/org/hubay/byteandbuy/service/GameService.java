package org.hubay.byteandbuy.service;

import lombok.Getter;
import org.hubay.byteandbuy.dto.GameStateDto;
import org.hubay.byteandbuy.dto.GameStateMapper;
import org.hubay.byteandbuy.dto.GameUpdateMessage;
import org.hubay.byteandbuy.dto.PlayerSummary;
import org.hubay.byteandbuy.dto.PlayerSummaryMapper;
import org.hubay.byteandbuy.entity.GameEntity;
import org.hubay.byteandbuy.event.GameEventCollector;
import org.hubay.byteandbuy.event.GameMessagePublisher;
import org.hubay.byteandbuy.exception.ConcurrentGameUpdateException;
import org.hubay.byteandbuy.factory.GameBuilder;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.persistence.mapper.GameSnapshotMapper;
import org.hubay.byteandbuy.persistence.snapshot.GameSnapshot;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Function;


/**
 * Hlavna aplikcna service trieda.
 * Prepaja vytvorenie objektov (GameBuilder), hernu logiku (GameEngine) a DB vrstvu.
 */
@Getter
@Service
public class GameService {
    private final GameBuilder gameBuilder;
    private final GamePersistenceService persistenceService;
    private final SnapshotSerializer serializer;
    private final GameEngine engine;
    private final GameMessagePublisher messagePublisher;

    public GameService(GameBuilder gameBuilder,
                       GamePersistenceService persistenceService,
                       SnapshotSerializer serializer,
                       GameEngine engine,
                       GameMessagePublisher messagePublisher) {
        this.gameBuilder = gameBuilder;
        this.persistenceService = persistenceService;
        this.serializer = serializer;
        this.engine = engine;
        this.messagePublisher = messagePublisher;
    }

    /**
     * Vytvory novu prazdnu hru a ulozi do DB.
     */
    public String createGame() {
        Game game = gameBuilder.createEmptyGame();
        return persistenceService.save(toEntity(game)).getId().toString();
    }

    /**
     * Nacita hru z DB a vytvori objekt Game.
     */
    public GameStateDto getGame(UUID gameId) {
        GameEntity entity = persistenceService.get(gameId);
        return GameStateMapper.toDto(loadGame(entity));
    }

    /**
     * Hrac kupi policko na ktorom stoji.
     */
    @Transactional
    public GameUpdateMessage buyProperty(UUID gameId, UUID playerId) {
        return execute(gameId, game -> {
            validateCurrentPlayerAction(game, playerId);
            engine.buyProperty(game);
            return null;
        });
    }

    /**
     * Hráč odmietne kúpu nehnuteľnosti.
     * Hrac nekupi policko na ktorom stoji.
     */
    @Transactional
    public GameUpdateMessage skipPurchase(UUID gameId, UUID playerId) {
        return execute(gameId, game -> {
            validateCurrentPlayerAction(game, playerId);
            engine.skipPurchase(game);
            return null;
        });
    }

    /**
     * Hrac sa odpoji z hry (napriklad bankrot alebo sa sam odpoji).
     */
    @Transactional
    public GameUpdateMessage leaveGame(UUID gameId, UUID playerId) {
        return execute(gameId, game -> {
            Player player = validatePlayerAction(game, playerId);
            engine.leaveGame(game, player);
            return null;
        });
    }

    /**
     * Heac hodi kockou a posunie sa.
     */
    @Transactional
    public GameUpdateMessage roll(UUID gameId, UUID playerId) {
        return execute(gameId, game -> {
            if (!game.isFinished()) {
                validateCurrentPlayerAction(game, playerId);
            }
            engine.roll(game);
            return null;
        });
    }

    /**
     * Hrac si potiahne kartu.
     */
    @Transactional
    public GameUpdateMessage drawCard(UUID gameId, UUID playerId) {
        return execute(gameId, game -> {
            validateCurrentPlayerAction(game, playerId);
            engine.drawCard(game);
            return null;
        });
    }

    /**
     * Prida hraca do hry.
     */
    @Transactional
    public GameUpdateMessage joinGame(UUID gameId, String playerName) {
        return execute(gameId, game -> {
            validatePlayerCanJoin(game, playerName);

            Player player = gameBuilder.createPlayer(playerName.trim());
            game.addPlayer(player);
            return PlayerSummaryMapper.toSummary(player);
        });
    }

    /**
     * Spusti hru.
     */
    @Transactional
    public GameUpdateMessage startGame(UUID gameId) {
        return execute(gameId, game -> {
            if (!game.isWaitingForPlayers()) {
                throw new IllegalStateException("Hra necaka na pripojenie hracov");
            }

            if (!game.canStart()) {
                throw new IllegalStateException("Nie je dost hracov pripojenych na zacatie hry");
            }

            game.resumePlaying();
            game.setCurrentPlayerIndex(0);
            return null;
        });
    }

    /**
     * Vytvori GameEntity z aktualneho stavu hry pre ulozenie do DB.
     */
    private GameEntity toEntity(Game game) {
        GameEntity entity = new GameEntity();
        updateEntity(entity, game);
        return entity;
    }

    /**
     * Prevedie objekt Game na GameEntity pre ulozenie do DB.
     */
    private void updateEntity(GameEntity entity, Game game) {
        entity.setState(game.getState());
        entity.setSnapshot(serializer.toJson(GameSnapshotMapper.toSnapshot(game)));
    }

    /**
     * GameEntity prevedie na objekt Game pre obnovenie ulozenej hry.
     */
    private Game loadGame(GameEntity entity) {
        GameSnapshot snapshot = serializer.fromJson(entity.getSnapshot());
        Game game = gameBuilder.createGame(snapshot);
        GameSnapshotMapper.applySnapshot(game, snapshot);
        return game;
    }

    /**
     * Wrapper metoda na vykonanie vsetkych hernych akcii.
     * - Nacitanie hry z DB
     * - vykonanie akcie (buyProperty, skipPurchase, roll, ...)
     * - ulozenie zmeny stavu
     */
    private GameUpdateMessage execute(UUID gameId, Function<Game, PlayerSummary> action) {
        try {
            GameEntity entity = persistenceService.get(gameId);
            Game game = loadGame(entity);
            GameEventCollector collector = new GameEventCollector();
            game.setEventCollector(collector);

            PlayerSummary player = action.apply(game);

            updateEntity(entity, game);
            persistenceService.save(entity);
            GameUpdateMessage message = new GameUpdateMessage(
                    "GAME_UPDATED",
                    gameId,
                    GameStateMapper.toDto(game),
                    collector.getEvents(),
                    player
            );
            messagePublisher.publishAfterCommit(message);

            return message;
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new ConcurrentGameUpdateException("Hra bola medzicasom zmenena inym requestom. Skus akciu zopakovat.", ex);
        }
    }

    /**
     * Validuje pravidla pre pridanie harac.
     */
    private void validatePlayerCanJoin(Game game, String playerName) {
        if (!game.isWaitingForPlayers()) {
            throw new IllegalStateException("Hra uz zacala");
        }
        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Meno hraca je povinne");
        }

        String normalizedName = playerName.trim();
        for (Player player : game.getPlayers()) {
            if (player.getName().equalsIgnoreCase(normalizedName)) {
                throw new IllegalArgumentException("Meno hraca uz existuje");
            }
        }
    }

    /**
     * Overi, ze request obsahuje existujuceho a aktivneho hraca.
     */
    private Player validatePlayerAction(Game game, UUID playerId) {
        if (playerId == null) {
            throw new IllegalArgumentException("ID hraca je povinne");
        }

        Player player = game.getPlayerById(playerId);
        if (player == null) {
            throw new IllegalArgumentException("Hrac neexistuje v tejto hre");
        }
        if (!player.isInGame()) {
            throw new IllegalStateException("Hrac uz nie je v hre");
        }

        return player;
    }

    /**
     * Overi, ze akciu vykonava hrac, ktory je prave na tahu.
     */
    private void validateCurrentPlayerAction(Game game, UUID playerId) {
        Player player = validatePlayerAction(game, playerId);
        Player currentPlayer = game.getCurrentPlayer();

        if (!currentPlayer.getId().equals(player.getId())) {
            throw new IllegalStateException("Nie si na tahu");
        }
    }
}
