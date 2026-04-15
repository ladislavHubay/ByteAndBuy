package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.springframework.stereotype.Service;

// Riesi pohyb hraca
@Service
public class MovementService {
    // Posunie hraca podla hodu kockou na nove hracie policko.
    public void movePlayer(Game game, Player player, int steps) {
        game.movePlayer(player, steps);
    }
}