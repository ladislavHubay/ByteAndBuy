package org.hubay.byteandbuy.dto;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Sprava posielana cez WebSocket po zmene stavu hry.
 */
@Getter
public class GameUpdateMessage {
    private final String type;
    private final UUID gameId;
    private final GameStateDto game;
    private final List<String> events;
    private final PlayerSummary player;

    public GameUpdateMessage(String type, UUID gameId, GameStateDto game, List<String> events, PlayerSummary player) {
        this.type = type;
        this.gameId = gameId;
        this.game = game;
        this.events = events;
        this.player = player;
    }
}
