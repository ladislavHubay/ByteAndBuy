package org.hubay.byteandbuy.event;

import org.hubay.byteandbuy.dto.GameUpdateMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Odosiela spravy pre frontend az po uspesnom ulozeni do DB.
 */
@Service
public class GameMessagePublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public GameMessagePublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Odosiela aktualny stav hry a udalosti az po uspesnom ulozeni do DB.
     */
    public void publishAfterCommit(GameUpdateMessage message) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            publish(message);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publish(message);
            }
        });
    }

    /**
     * Odosle websocket na topic kontajner.
     */
    private void publish(GameUpdateMessage message) {
        messagingTemplate.convertAndSend(GameTopics.game(message.getGameId()), message);
    }
}
