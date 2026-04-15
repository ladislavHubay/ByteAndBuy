package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.springframework.stereotype.Service;

@Service
public class JailService {
    public boolean handleJailTurn(Game game, Player player, int dice) {
        if (!player.isInJail()) {
            return false;
        }

        game.setDice(dice);

        if (dice == 6) {
            player.setInJail(false);
        } else {
            System.out.println(player.getName() + " zostava vo vazani");
        }

        return true;
    }
}
