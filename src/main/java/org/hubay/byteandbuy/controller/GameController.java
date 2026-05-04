package org.hubay.byteandbuy.controller;

import org.hubay.byteandbuy.dto.JoinGameRequest;
import org.hubay.byteandbuy.dto.PlayerSummary;
import org.hubay.byteandbuy.dto.TurnResponse;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.service.GameService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller pre endpointy pre riadenie hry.
 */
@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/{gameId}/buy")
    public TurnResponse buyProperty(@PathVariable String gameId) {
        return gameService.buyProperty(gameId);
    }

    @PostMapping("/{gameId}/skip")
    public TurnResponse skip(@PathVariable String gameId) {
        return gameService.skipPurchase(gameId);
    }

    @PostMapping("/{gameId}/leave")
    public Game leaveGame(@PathVariable String gameId) {
        return gameService.leaveGame(gameId);
    }

    @PostMapping("/create")
    public String createGame() {
        return gameService.createGame();
    }

    @PostMapping("/{gameId}/join")
    public PlayerSummary joinGame(@PathVariable String gameId, @RequestBody JoinGameRequest request) {
        return gameService.joinGame(gameId, request.getPlayerName());
    }

    @PostMapping("/{gameId}/start")
    public Game startGame(@PathVariable String gameId) {
        return gameService.startGame(gameId);
    }

    @PostMapping("/{gameId}/roll")
    public TurnResponse roll(@PathVariable String gameId) {
        return gameService.roll(gameId);
    }

    @PostMapping("/{gameId}/draw")
    public TurnResponse drawCard(@PathVariable String gameId) {
        return gameService.drawCard(gameId);
    }

    @GetMapping("/{gameId}/state")
    public Game getState(@PathVariable String gameId) {
        return gameService.getGame(gameId);
    }
}
