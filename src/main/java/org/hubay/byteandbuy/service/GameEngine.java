package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.dto.PlayerSummaryMapper;
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
     * Vykona cely tah hraca.
     * - skontroluje ci hra neskoncila
     * - vykona hod kockou
     * - zabespeci riesenie vazanie
     * - posunie hraca na potrebnu poziciu a vyhodnoti efekt policka
     * - ukonci tah hraca
     */
    public TurnResponse roll(Game game) {
        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);
        TurnResponse response = new TurnResponse();

        if (game.isFinished()) {
            collector.add("Hra je ukoncena");
            response.setEvents(collector.getEvents());
            return response;
        }

        if (!game.isPlaying()) {
            throw new IllegalStateException("Decision expected");
        }

        Player player = game.getCurrentPlayer();
        response.setFromPosition(player.getPosition());
        response.setCurrentPlayer(player.getName());
        collector.add(player.getName() + " je na pozicii " +
                game.getCurrentTile(player).getName() +
                ", ma: " + player.getMoney());

        int dice = rollDice();
        game.setDice(dice);
        response.setDice(dice);
        collector.add("Hodil si: " + dice);

        if (handleJailRoll(game, player, dice, response, collector)) {
            return response;
        }

        resolveRollMovement(game, player, dice, response, collector);
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
        if (!game.isWaitingForBuy()) {
            throw new IllegalStateException("No decision expected");
        }

        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);
        TurnResponse response = new TurnResponse();
        Player player = game.getCurrentPlayer();
        Tile tile = game.getCurrentTile(player);
        response.setCurrentPlayer(player.getName());
        response.setTileName(tile.getName());

        economyService.buyProperty(game, player);
        game.resumePlaying();

        completeAction(game, player, response, collector);

        return response;
    }

    /**
     * Spracuje rozhodnutie hraca nekupit policko.
     */
    public TurnResponse skipPurchase(Game game) {
        if (!game.isWaitingForBuy()) {
            throw new IllegalStateException("No decision expected");
        }

        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);
        TurnResponse response = new TurnResponse();
        Player player = game.getCurrentPlayer();

        response.setCurrentPlayer(player.getName());
        collector.add(player.getName() + " nekupil policko");

        game.resumePlaying();

        completeAction(game, player, response, collector);

        return response;
    }

    /**
     * Spracuje rozhodnutie hraca odist z hry.
     * Hra pokracuje pokial v hre zostavaju minimalne dvaja hraci.
     */
    public void leaveGame(Game game, Player player) {
        if (game.isWaitingForPlayers()) {
            player.setInGame(false);
            return;
        }

        boolean currentPlayerLeft = game.getCurrentPlayer().getId().equals(player.getId());
        playerStateService.removePlayerFromGame(game, player);

        if (!game.isFinished() && currentPlayerLeft) {
            game.resumePlaying();
            turnService.moveToNextPlayer(game);
        }
    }

    /**
     * Spracuje tahanie karty ak hrac stoji na policku kde sa ma potiahnut karta.
     */
    public TurnResponse drawCard(Game game) {
        if (!game.isWaitingForCard()) {
            throw new IllegalStateException("No decision expected");
        }

        Player player = game.getCurrentPlayer();
        TurnResponse response = new TurnResponse();

        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);

        response.setCurrentPlayer(player.getName());

        drawAndResolveCardEffects(game, player);
        completeAction(game, player, response, collector);

        return response;
    }

    /**
     * Spracuje tah hraca vo vazani.
     * Ak bol tah ukonceny vo vazani vrati true.
     */
    private boolean handleJailRoll(Game game, Player player, int dice, TurnResponse response, GameEventCollector collector) {
        if (!jailService.handleJailTurn(game, player, dice)) {
            return false;
        }

        completeAction(game, player, response, collector);
        return true;
    }

    /**
     * Hrac potiahne kartu a vykona efekt karty.
     * Ak karta zmenila poziciu hraca, vyhodnoti sa dalsie policko.
     */
    private void drawAndResolveCardEffects(Game game, Player player) {
        int oldPosition = player.getPosition();
        tileActionService.drawCard(game, player);

        if (oldPosition != player.getPosition()) {
            tileActionService.resolveTileEffects(game, player);
        }
    }

    /**
     * Posunie hraca a vyhodnoti efekt policka na ktorom hrac po posunuti stoji.
     */
    private void resolveRollMovement(Game game, Player player, int dice, TurnResponse response, GameEventCollector collector) {
        movementService.movePlayer(game, player, dice);
        response.setToPosition(player.getPosition());
        response.setTileName(game.getCurrentTile(player).getName());
        collector.add(player.getName() + " sa posunul na " + game.getCurrentTile(player).getName());
        tileActionService.resolveTileEffects(game, player);
    }

    /**
     * Ukonci tah hraca a pripravy odpoved pre frontend.
     */
    private void completeAction(Game game, Player player, TurnResponse response, GameEventCollector collector) {
        turnService.finishTurn(game, player);
        response.setEvents(collector.getEvents());
        response.setPlayers(PlayerSummaryMapper.toSummaries(game.getPlayers()));
    }
}
