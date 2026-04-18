package org.hubay.byteandbuy.service;

import lombok.Getter;
import org.hubay.byteandbuy.factory.GameFactory;
import org.hubay.byteandbuy.model.game.Game;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Service
public class GameService {
    private final GameFactory gameFactory;
    private final Map<String, Game> games = new HashMap<>();

    public GameService(GameFactory gameFactory) {
        this.gameFactory = gameFactory;
    }

    public String createGame() {
        String gameId = UUID.randomUUID().toString();
        games.put(gameId, gameFactory.createSampleGame());
        return gameId;
    }

    public Game getGame(String gameId) {
        Game game = games.get(gameId);

        if (game == null) {
            throw new IllegalArgumentException("Game not found");
        }

        return game;
    }
}
