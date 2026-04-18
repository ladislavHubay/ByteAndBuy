package org.hubay.byteandbuy.service;

import lombok.Getter;
import org.hubay.byteandbuy.factory.GameFactory;
import org.hubay.byteandbuy.model.game.Game;
import org.springframework.stereotype.Service;

@Getter
@Service
public class GameService {
    private final Game game;

    public GameService(GameFactory factory) {
        this.game = factory.createSampleGame();
    }
}
