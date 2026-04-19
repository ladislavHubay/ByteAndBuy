package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.dto.TurnResponse;
import org.hubay.byteandbuy.event.GameEventCollector;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Trieda orchestruje priebeh tahu hraca.
 */
@Service
public class GameEngine {
    private final Random random;
    private final TileActionService tileActionService;
    private final PlayerStateService playerStateService;
    private final TurnService turnService;
    private final MovementService movementService;
    private final EconomyService economyService;
    private final JailService jailService;

    public GameEngine(Random random, TileActionService tileActionService, PlayerStateService playerStateService, TurnService turnService, MovementService movementService, EconomyService economyService, JailService jailService) {
        this.random = random;
        this.tileActionService = tileActionService;
        this.playerStateService = playerStateService;
        this.turnService = turnService;
        this.movementService = movementService;
        this.economyService = economyService;
        this.jailService = jailService;
    }

    /**
     * Vygeneruje nahodne cislo 1 az 6. Simuluje hod kockou.
     */
    public int rollDice(){
        return random.nextInt(6) + 1;
    }

    /**
     * Vykona jeden tah aktualneho hraca.
     * Vrati vysledok vo forme TurnResponse.
     */
    public TurnResponse playTurn(Game game) {
        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);

        Player player = game.getCurrentPlayer();
        TurnResponse response = new TurnResponse();

        response.setCurrentPlayer(player.getName());
        response.setFromPosition(player.getPosition());

        collector.add(player.getName() + " je na pozicii " +
                game.getCurrentTile(player).getName() +
                ", ma: " + player.getMoney());

        int dice = rollDice();
        game.setDice(dice);

        response.setDice(dice);

        collector.add(player.getName() + " hodil " + dice);

        if (jailService.handleJailTurn(game, player, dice)) {
            turnService.finishTurn(game);
            response.setEvents(collector.getEvents());
            return response;
        }

        movementService.movePlayer(game, player, dice);

        response.setToPosition(player.getPosition());
        response.setTileName(game.getCurrentTile(player).getName());

        tileActionService.resolveTileEffects(game, player);

        response.setMoney(player.getMoney());

        boolean shouldEndTurn = turnService.shouldEndTurn(game, player);
        response.setExtraTurn(shouldEndTurn);

        turnService.finishTurn(game);

        response.setNextPlayer(game.getCurrentPlayer().getName());
        response.setEvents(collector.getEvents());

        return response;
    }

    /**
     * Spracuje rozhodnutie hraca kupit policko.
     */
    public TurnResponse buyProperty(Game game) {
        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);

        Player player = game.getCurrentPlayer();
        Tile tile = game.getCurrentTile(player);

        TurnResponse response = new TurnResponse();
        response.setCurrentPlayer(player.getName());
        response.setTileName(tile.getName());

        economyService.buyProperty(game);

        int moneyAfter = player.getMoney();
        response.setMoney(moneyAfter);

        turnService.finishTurn(game);

        response.setNextPlayer(game.getCurrentPlayer().getName());
        response.setEvents(collector.getEvents());

        return response;
    }

    /**
     * Spracuje rozhodnutie hraca nekupit policko.
     */
    public TurnResponse skipPurchase(Game game) {
        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);

        Player player = game.getCurrentPlayer();

        if (!game.isWaitingForDecision()) {
            throw new IllegalStateException("No decision expected");
        }

        TurnResponse response = new TurnResponse();
        response.setCurrentPlayer(player.getName());

        collector.add(player.getName() + " nekupil policko");

        game.resumePlaying();

        turnService.finishTurn(game);

        response.setNextPlayer(game.getCurrentPlayer().getName());
        response.setEvents(collector.getEvents());

        return response;
    }

    /**
     * Spracuje rozhodnutie hraca odist z hry.
     * Hra pokracuje pokial v hre zostavaju minimalne dvaja hraci.
     */
    public void leaveGame(Game game) {
        Player player = game.getCurrentPlayer();

        player.setInGame(false);
        game.resumePlaying();

        playerStateService.removePlayerFromGame(game, player);

        if (game.isFinished()) {
            turnService.moveToNextPlayer(game);
        }
    }
}