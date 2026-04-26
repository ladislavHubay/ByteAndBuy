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
        GameEventCollector collector = createCollector(game);

        if (game.isFinished()) {
            collector.add("Hra je ukoncena");
            return createFinishedResponse(collector);
        }

        Player player = game.getCurrentPlayer();
        TurnResponse response = createRollResponseFor(player);

        collector.add(player.getName() + " je na pozicii " +
                game.getCurrentTile(player).getName() +
                ", ma: " + player.getMoney());

        int dice = rollDice();
        game.setDice(dice);
        response.setDice(dice);

        collector.add("Hodil si: " + dice);

        if (jailService.handleJailTurn(game, player, dice)) {
            completeAction(game, player, response, collector);
            return response;
        }

        movementService.movePlayer(game, player, dice);

        response.setToPosition(player.getPosition());
        response.setTileName(game.getCurrentTile(player).getName());

        collector.add(player.getName() + " sa posunul na " + game.getCurrentTile(player).getName());

        tileActionService.resolveTileEffects(game, player);
        response.setMoney(player.getMoney());
        completeAction(game, player, response, collector);

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
        GameEventCollector collector = createCollector(game);

        Player player = game.getCurrentPlayer();
        Tile tile = game.getCurrentTile(player);

        TurnResponse response = createResponseFor(player);
        response.setTileName(tile.getName());

        economyService.buyProperty(game, player);
        game.resumePlaying();

        response.setMoney(player.getMoney());
        completeAction(game, player, response, collector);

        return response;
    }

    /**
     * Spracuje rozhodnutie hraca nekupit policko.
     */
    public TurnResponse skipPurchase(Game game) {
        GameEventCollector collector = createCollector(game);

        Player player = game.getCurrentPlayer();

        if (!game.isWaitingForBuy()) {
            throw new IllegalStateException("No decision expected");
        }

        TurnResponse response = createResponseFor(player);

        collector.add(player.getName() + " nekupil policko");

        game.resumePlaying();

        completeAction(game, player, response, collector);

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

        ensureWaitingForCard(game);

        GameEventCollector collector = createCollector(game);
        TurnResponse response = createResponseFor(player);

        drawAndResolveCardEffects(game, player);

        completeAction(game, player, response, collector);

        return response;
    }

    private GameEventCollector createCollector(Game game) {
        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);
        return collector;
    }

    private TurnResponse createResponseFor(Player player) {
        TurnResponse response = new TurnResponse();
        response.setCurrentPlayer(player.getName());
        return response;
    }

    private TurnResponse createRollResponseFor(Player player) {
        TurnResponse response = createResponseFor(player);
        response.setFromPosition(player.getPosition());
        response.setMoney(player.getMoney());
        return response;
    }

    private TurnResponse createFinishedResponse(GameEventCollector collector) {
        TurnResponse response = new TurnResponse();
        response.setEvents(collector.getEvents());
        return response;
    }

    private void ensureWaitingForCard(Game game) {
        if (!game.isWaitingForCard()) {
            throw new IllegalStateException("No decision expected");
        }
    }

    private void drawAndResolveCardEffects(Game game, Player player) {
        int oldPosition = player.getPosition();
        tileActionService.drawCard(game, player);

        if (oldPosition != player.getPosition()) {
            tileActionService.resolveTileEffects(game, player);
        }
    }

    private void completeAction(Game game, Player player, TurnResponse response, GameEventCollector collector) {
        turnService.finishTurn(game, player);
        finalizeResponse(response, game, collector);
    }

    private void finalizeResponse(TurnResponse response, Game game, GameEventCollector collector) {
        response.setNextPlayer(game.getCurrentPlayer().getName());
        response.setEvents(collector.getEvents());
    }
}
