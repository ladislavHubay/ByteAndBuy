package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.dto.PlayerSummary;
import org.hubay.byteandbuy.dto.TurnResponse;
import org.hubay.byteandbuy.event.GameEventCollector;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            return createFinishedResponse(collector);
        }

        Player player = game.getCurrentPlayer();
        TurnResponse response = createRollResponseFor(player);

        logRollStart(game, player, collector);
        int dice = performRoll(game, response, collector);

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
        GameEventCollector collector = createCollector(game);
        Player player = game.getCurrentPlayer();
        Tile tile = game.getCurrentTile(player);
        TurnResponse response = createResponseFor(player);
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

    /**
     * Spracuje tahanie karty ak hrac stoji na policku kde sa ma potiahnut karta.
     */
    public TurnResponse drawCard(Game game) {
        Player player = game.getCurrentPlayer();

        ensureWaitingForCard(game);

        GameEventCollector collector = createCollector(game);
        TurnResponse response = createResponseFor(player);

        drawAndResolveCardEffects(game, player);
        completeAction(game, player, response, collector);

        return response;
    }

    /**
     * Vytvori objekt GameEventCollector.
     */
    private GameEventCollector createCollector(Game game) {
        GameEventCollector collector = new GameEventCollector();
        game.setEventCollector(collector);
        return collector;
    }

    /**
     * Vytvori objekt TurnResponse a ulozi do neho aktualneho hraca pre ukladanie informacii pre frontend.
     */
    private TurnResponse createResponseFor(Player player) {
        TurnResponse response = new TurnResponse();
        response.setCurrentPlayer(player.getName());
        return response;
    }

    /**
     * Do objektu TurnResponse a ulozi poziciu a aktualny stav financii hraca.
     */
    private TurnResponse createRollResponseFor(Player player) {
        TurnResponse response = createResponseFor(player);
        response.setFromPosition(player.getPosition());
        return response;
    }

    /**
     * Vytvori odpoved pre frontend v pripade ukoncenia hry.
     */
    private TurnResponse createFinishedResponse(GameEventCollector collector) {
        TurnResponse response = new TurnResponse();
        collector.add("Hra je ukoncena");
        response.setEvents(collector.getEvents());
        return response;
    }

    /**
     * Do objektu GameEventCollector ulozi pre info startovaciu poziciu hraca,
     * meno hraca a sumu z akou hrac zacina konkretny tah.
     */
    private void logRollStart(Game game, Player player, GameEventCollector collector) {
        collector.add(player.getName() + " je na pozicii " +
                game.getCurrentTile(player).getName() +
                ", ma: " + player.getMoney());
    }

    /**
     * Vykona hod kockou a ulozi pre potreby frontendu.
     */
    private int performRoll(Game game, TurnResponse response, GameEventCollector collector) {
        int dice = rollDice();
        game.setDice(dice);
        response.setDice(dice);
        collector.add("Hodil si: " + dice);
        return dice;
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
     * Skontroluje ci hrac caka na tahanie karty.
     */
    private void ensureWaitingForCard(Game game) {
        if (!game.isWaitingForCard()) {
            throw new IllegalStateException("No decision expected");
        }
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
        finalizeResponse(response, game, collector);
    }

    /**
     * Z finalizuje zoznam udalosti pre odpoved pre frontend.
     */
    private void finalizeResponse(TurnResponse response, Game game, GameEventCollector collector) {
        response.setEvents(collector.getEvents());
        response.setPlayers(mapPlayers(game));
    }

    /**
     * Prevedie zoznam hracov so zakladnymi informaciami do DTO objektu pre frontend.
     * Metoda vrati zoznam vsetkych hracov bezohladu na to ci este hraju alebo uz dohrali.
     */
    private List<PlayerSummary> mapPlayers(Game game) {
        List<PlayerSummary> result = new ArrayList<>();

        for (Player player : game.getPlayers()) {
            PlayerSummary dto = new PlayerSummary();
            dto.setName(player.getName());
            dto.setMoney(player.getMoney());
            dto.setPosition(player.getPosition());
            dto.setInGame(player.isInGame());
            dto.setInJail(player.isInJail());

            result.add(dto);
        }

        return result;
    }
}
