package org.hubay.byteandbuy.controller;

import org.hubay.byteandbuy.dto.TurnResponse;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.service.GameEngine;
import org.hubay.byteandbuy.service.GameService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller pre endpointy pre riadenie hry.
 */
@RestController
@RequestMapping("/game")
public class GameController {
    private final GameEngine engine;
    private final GameService gameService;

    public GameController(GameEngine engine, GameService gameService) {
        this.engine = engine;
        this.gameService = gameService;
    }

    @PostMapping("/{gameId}/turn")
    public TurnResponse playTurn(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);
        return engine.playTurn(game);
    }

    @PostMapping("/{gameId}/buy")
    public TurnResponse buyProperty(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);
        return engine.buyProperty(game);
    }

    @PostMapping("/{gameId}/skip")
    public TurnResponse skip(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);
        return engine.skipPurchase(game);
    }

    @PostMapping("/{gameId}/leave")
    public Game leaveGame(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);
        engine.leaveGame(game);
        return game;
    }

    @GetMapping("/{gameId}/state")
    public Game getState(@PathVariable String gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping("/create")
    public String createGame() {
        return gameService.createGame();
    }
}
