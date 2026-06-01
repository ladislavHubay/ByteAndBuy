package org.hubay.byteandbuy.controller;

import org.hubay.byteandbuy.dto.GameStateDto;
import org.hubay.byteandbuy.dto.GameUpdateMessage;
import org.hubay.byteandbuy.dto.JoinGameRequest;
import org.hubay.byteandbuy.dto.PlayerActionRequest;
import org.hubay.byteandbuy.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public GameUpdateMessage buyProperty(@PathVariable UUID gameId, @RequestBody PlayerActionRequest request) {
        return gameService.buyProperty(gameId, request.getPlayerId());
    }

    @PostMapping("/{gameId}/skip")
    public GameUpdateMessage skip(@PathVariable UUID gameId, @RequestBody PlayerActionRequest request) {
        return gameService.skipPurchase(gameId, request.getPlayerId());
    }

    @PostMapping("/{gameId}/leave")
    public GameUpdateMessage leaveGame(@PathVariable UUID gameId, @RequestBody PlayerActionRequest request) {
        return gameService.leaveGame(gameId, request.getPlayerId());
    }

    @PostMapping("/create")
    public String createGame() {
        return gameService.createGame();
    }

    @PostMapping("/{gameId}/join")
    public GameUpdateMessage joinGame(@PathVariable UUID gameId, @RequestBody JoinGameRequest request) {
        return gameService.joinGame(gameId, request.getPlayerName());
    }

    @PostMapping("/{gameId}/start")
    public GameUpdateMessage startGame(@PathVariable UUID gameId) {
        return gameService.startGame(gameId);
    }

    @PostMapping("/{gameId}/roll")
    public GameUpdateMessage roll(@PathVariable UUID gameId, @RequestBody PlayerActionRequest request) {
        return gameService.roll(gameId, request.getPlayerId());
    }

    @PostMapping("/{gameId}/draw")
    public GameUpdateMessage drawCard(@PathVariable UUID gameId, @RequestBody PlayerActionRequest request) {
        return gameService.drawCard(gameId, request.getPlayerId());
    }

    @GetMapping("/{gameId}/state")
    public GameStateDto getState(@PathVariable UUID gameId) {
        return gameService.getGame(gameId);
    }
}
