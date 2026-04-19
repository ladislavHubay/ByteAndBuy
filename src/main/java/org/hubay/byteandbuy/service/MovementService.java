package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.springframework.stereotype.Service;

/**
 * Spracuje pohyb hraca.
 */
@Service
public class MovementService {
    /**
     *  Deleguje vykonanie pohybu hraca na triedu Game
     */
    public void movePlayer(Game game, Player player, int steps) {
        game.movePlayer(player, steps, true);
    }
}