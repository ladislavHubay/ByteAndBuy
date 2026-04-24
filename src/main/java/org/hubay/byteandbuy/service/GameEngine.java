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

    public TurnResponse roll(Game game) {
        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);

        TurnResponse response = new TurnResponse();

        if (game.isFinished()) {
            collector.add("Hra je ukoncena");
            response.setEvents(collector.getEvents());
            return response;
        }

        Player player = game.getCurrentPlayer();
        response.setCurrentPlayer(player.getName());
        response.setFromPosition(player.getPosition());
        response.setMoney(player.getMoney());

        collector.add(player.getName() + " je na pozicii " +
                game.getCurrentTile(player).getName() +
                ", ma: " + player.getMoney());

        int dice = rollDice();
        game.setDice(dice);
        response.setDice(dice);

        collector.add("Hodil si: " + dice);

        if (jailService.handleJailTurn(game, player, dice)) {
            turnService.finishTurn(game, player);
            response.setEvents(collector.getEvents());
            return response;
        }

        movementService.movePlayer(game, player, dice);

        response.setToPosition(player.getPosition());
        response.setTileName(game.getCurrentTile(player).getName());

        collector.add(player.getName() + " sa posunul na " + game.getCurrentTile(player).getName());

        tileActionService.resolveTileEffects(game, player);
        turnService.finishTurn(game, player);

        response.setEvents(collector.getEvents());

        return response;
    }

    /**
     * Vygeneruje nahodne cislo 1 az 6. Simuluje hod kockou.
     */
    public int rollDice(){
        return random.nextInt(6) + 1;
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

        economyService.buyProperty(game, player);

        int moneyAfter = player.getMoney();
        response.setMoney(moneyAfter);

        game.resumePlaying();

        turnService.finishTurn(game, player);

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

        turnService.finishTurn(game, player);

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

        playerStateService.removePlayerFromGame(game, player);

        if (!game.isFinished()) {
            game.resumePlaying();
            turnService.moveToNextPlayer(game);
        }
    }

    public TurnResponse drawCard(Game game) {
        Player player = game.getCurrentPlayer();
        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);

        TurnResponse response = new TurnResponse();

        int oldPosition = player.getPosition();
        tileActionService.drawCard(game, player);

        if (oldPosition != player.getPosition()) {
            tileActionService.resolveTileEffects(game, player);
        }

        response.setEvents(collector.getEvents());

        turnService.finishTurn(game, player);
        response.setNextPlayer(game.getCurrentPlayer().getName());

        return response;
    }
}