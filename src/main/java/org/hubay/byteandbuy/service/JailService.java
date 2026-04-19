package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.springframework.stereotype.Service;

/**
 * Spravanie hry pre hravca ktory je vo vazni.
 */
@Service
public class JailService {
    /**
     * Spravuje tah hraca vo vazani.
     * Vyhodnocuje ci hrac zostava vo vazani alebo sa dostal z vazania.
     */
    public boolean handleJailTurn(Game game, Player player, int dice) {
        if (!player.isInJail()) {
            return false;
        }

        if (dice == 6) {
            player.setInJail(false);
            game.getEventCollector().add(player.getName() + " sa dostal z vazenia");
        } else {
            game.getEventCollector().add(player.getName() + " zostava vo vazeni");
        }

        return true;
    }
}
