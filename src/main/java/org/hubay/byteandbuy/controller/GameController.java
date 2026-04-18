package org.hubay.byteandbuy.controller;

import org.hubay.byteandbuy.dto.TurnResponse;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.service.GameEngine;
import org.hubay.byteandbuy.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameEngine engine;
    private final GameService gameService;

    public GameController(GameEngine engine, GameService gameService) {
        this.engine = engine;
        this.gameService = gameService;
    }

    // Spustenie jedného ťahu
    // http://localhost:8080/game/{gameId}/turn
    @PostMapping("/{gameId}/turn")
    public TurnResponse playTurn(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);
        return engine.playTurn(game);
    }

    // spusti nakup policka na ktorom hrac stoji
    // http://localhost:8080/game/{gameId}/buy
    @PostMapping("/{gameId}/buy")
    public TurnResponse buyProperty(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);
        return engine.buyProperty(game);
    }

    // Volba - nekupit policko
    // http://localhost:8080/game/{gameId}/skip
    @PostMapping("/{gameId}/skip")
    public TurnResponse skip(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);
        return engine.skipPurchase(game);
    }

    // Hrac ukonci svoju hru. Ostatny hraci hraju dalej.
    // http://localhost:8080/game/{gameId}/leave
    @PostMapping("/{gameId}/leave")
    public Game leaveGame(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);
        engine.leaveGame(game);
        return game;
    }

    // Získať aktuálny stav hry (bez zmeny)
    // http://localhost:8080/game/{gameId}/state
    @GetMapping("/{gameId}/state")
    public Game getState(@PathVariable String gameId) {
        return gameService.getGame(gameId);
    }

    // http://localhost:8080/game/create
    @PostMapping("/create")
    public String createGame() {
        return gameService.createGame();
    }
}
