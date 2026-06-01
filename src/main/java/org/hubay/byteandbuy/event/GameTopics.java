package org.hubay.byteandbuy.event;

import java.util.UUID;

/**
 * Vytvara websocket topic.
 */
public final class GameTopics {
    private static final String GAME_TOPIC_PREFIX = "/topic/game/";

    private GameTopics() {
    }

    /**
     * Vytvara websoket topic pre konkretnu hru s gameID.
     */
    public static String game(UUID gameId) {
        return GAME_TOPIC_PREFIX + gameId;
    }
}
