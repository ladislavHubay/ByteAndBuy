package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.springframework.stereotype.Service;

import java.util.Random;

// Herna logika.
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

    // metoda simuluje hod kockou.
    public int rollDice(){
        return random.nextInt(6) + 1;
    }

    // Metoda riadi (Orchestruje) cely tah hraca.
    public void playTurn(Game game) {
        Player player = game.getCurrentPlayer();
        System.out.println(player.getName() + " je na " + game.getCurrentTile(player).getName() +
                " na ucte ma: " + player.getMoney());

        if (game.isFinished()) {
            throw new IllegalStateException("Game is already finished");
        }

        int dice = rollDice();
        System.out.println(player.getName() + " hodil/-la " + dice);

        if (jailService.handleJailTurn(game, player, dice)) {
            turnService.finishTurn(game);
            return;
        }

        game.setDice(dice);

        movementService.movePlayer(game, player, dice);
        tileActionService.resolveTileEffects(game, player);
        turnService.finishTurn(game);
    }

    // Metoda sa vykona ked sa hrac rozhodne kupit policko.
    // vykona kupu policka.
    public void buyProperty(Game game) {
        economyService.buyProperty(game);
        turnService.finishTurn(game);
    }

    // Metoda sa vykona v pripade ak hrac sa rozhodne NEkupit policko.
    // Posunie hru dalej bez toho aby hrac kupil policko.
    public void skipPurchase(Game game) {
        if (!game.isWaitingForDecision()) {
            throw new IllegalStateException("No decision expected");
        }

        game.resumePlaying();
        System.out.println("nekupil policko");
        turnService.finishTurn(game);
    }

    // Ukonci hru pre daneho hraca.
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