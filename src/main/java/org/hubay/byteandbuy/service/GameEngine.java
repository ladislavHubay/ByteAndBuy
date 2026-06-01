package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.event.GameEventCollector;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
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

    public GameEngine(Random random,
                      TileActionService tileActionService,
                      PlayerStateService playerStateService,
                      TurnService turnService,
                      MovementService movementService,
                      EconomyService economyService,
                      JailService jailService) {
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
    public void roll(Game game) {
        GameEventCollector collector = getCollector(game);

        if (game.isFinished()) {
            collector.add("Hra je ukoncena");
            return;
        }

        if (!game.isPlaying()) {
            throw new IllegalStateException("Ocakava sa rozhodnutie");
        }

        Player player = game.getCurrentPlayer();
        collector.add(player.getName() + " je na pozicii " +
                game.getCurrentTile(player).getName() +
                ", ma: " + player.getMoney());

        int dice = rollDice();
        game.setDice(dice);
        collector.add("Hodil si: " + dice);

        if (handleJailRoll(game, player, dice)) {
            return;
        }

        resolveRollMovement(game, player, dice);
        completeAction(game, player);
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
    public void buyProperty(Game game) {
        if (!game.isWaitingForBuy()) {
            throw new IllegalStateException("Neocakava sa ziadne rozhodnutie");
        }

        Player player = game.getCurrentPlayer();

        economyService.buyProperty(game, player);
        game.resumePlaying();

        completeAction(game, player);
    }

    /**
     * Spracuje rozhodnutie hraca nekupit policko.
     */
    public void skipPurchase(Game game) {
        if (!game.isWaitingForBuy()) {
            throw new IllegalStateException("Neocakava sa ziadne rozhodnutie");
        }

        GameEventCollector collector = getCollector(game);
        Player player = game.getCurrentPlayer();

        collector.add(player.getName() + " nekupil policko");

        game.resumePlaying();

        completeAction(game, player);
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
    public void drawCard(Game game) {
        if (!game.isWaitingForCard()) {
            throw new IllegalStateException("Neocakava sa ziadne rozhodnutie");
        }

        Player player = game.getCurrentPlayer();

        drawAndResolveCardEffects(game, player);
        completeAction(game, player);
    }

    /**
     * Spracuje tah hraca vo vazani.
     * Ak bol tah ukonceny vo vazani vrati true.
     */
    private boolean handleJailRoll(Game game, Player player, int dice) {
        if (!jailService.handleJailTurn(game, player, dice)) {
            return false;
        }

        completeAction(game, player);
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
    private void resolveRollMovement(Game game, Player player, int dice) {
        GameEventCollector collector = getCollector(game);

        movementService.movePlayer(game, player, dice);
        collector.add(player.getName() + " sa posunul na " + game.getCurrentTile(player).getName());
        tileActionService.resolveTileEffects(game, player);
    }

    /**
     * Ukonci tah hraca.
     */
    private void completeAction(Game game, Player player) {
        turnService.finishTurn(game, player);
    }

    /**
     * Vrati collector udalosti aktualnej hry.
     */
    private GameEventCollector getCollector(Game game) {
        return game.getEventCollector();
    }
}
