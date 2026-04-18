package org.hubay.byteandbuy.controller;

import org.hubay.byteandbuy.dto.TurnResponse;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.service.GameEngine;
import org.hubay.byteandbuy.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // http://localhost:8080/game/turn
    @PostMapping("/turn")
    public TurnResponse playTurn() {
        Game game = gameService.getGame();
        return engine.playTurn(game);
    }

    // spusti nakup policka na ktorom hrac stoji
    // http://localhost:8080/game/buy
    @PostMapping("/buy")
    public TurnResponse buyProperty() {
        Game game = gameService.getGame();
        return engine.buyProperty(game);
    }

    // Volba - nekupit policko
    // http://localhost:8080/game/skip
    @PostMapping("/skip")
    public TurnResponse skip() {
        Game game = gameService.getGame();
        return engine.skipPurchase(game);
    }

    // Hrac ukonci svoju hru. Ostatny hraci hraju dalej.
    // http://localhost:8080/game/leave
    @PostMapping("/leave")
    public Game leaveGame() {
        Game game = gameService.getGame();
        engine.leaveGame(game);
        return game;
    }

    // Získať aktuálny stav hry (bez zmeny)
    // http://localhost:8080/game/state
    @GetMapping("/state")
    public Game getState() {
        return gameService.getGame();
    }
}
