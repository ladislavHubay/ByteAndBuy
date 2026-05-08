package org.hubay.byteandbuy.event;

import lombok.Getter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Sluzi na ukladanie logov.
 */
@Getter
public class GameEventCollector {
    private final SimpMessagingTemplate messagingTemplate;
    private final List<String> events = new ArrayList<>();
    private final UUID gameId;

    public GameEventCollector(SimpMessagingTemplate messagingTemplate, UUID gameId) {
        this.messagingTemplate = messagingTemplate;
        this.gameId = gameId;
    }

    /**
     * Prida hernu udalost do zoznamu a odosle cez WebSocket ostatnym pripojenym hracom.
     */
    public void add(String event) {
        events.add(event);
        messagingTemplate.convertAndSend("/topic/game/" + gameId, event);
    }
}
