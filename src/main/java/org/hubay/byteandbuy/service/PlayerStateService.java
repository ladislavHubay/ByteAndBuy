package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.BankruptAware;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.springframework.stereotype.Service;

/**
 * Trieda pre spravu stavu hracov.
 */
@Service
public class PlayerStateService {
    /**
     * Kontroluje ci hrac skrachoval.
     * Ak hracovy dojdu financne prostriedky, je vyradeny z hry.
     */
    public void checkBankruptcy(Game game, Player player) {
        if (player.getMoney() <= 0) {
            game.getEventCollector().add(player.getName() + " skrachoval");
            removePlayerFromGame(game, player);
        }
    }

    /**
     * Odstrani hraca z hry, nastavy vsetky jeho policka za znovu kupitelne
     * a skontroluje ci je este dostatok hracov v hre.
     */
    public void removePlayerFromGame(Game game, Player player) {
        player.setInGame(false);

        for (Tile tile : game.getBoard().getTiles()) {
            if (tile instanceof BankruptAware bankruptAware) {
                bankruptAware.onPlayerBankrupt(player);
            }
        }

        checkGameFinished(game);
    }

    /**
     * Skontroluje ci su v hre minimalne dvaja hraci.
     * Ak nie, hru nastavy na ukoncenu.
     */
    public void checkGameFinished(Game game){
        int activePlayers = 0;

        for (Player p : game.getPlayers()) {
            if (p.isInGame()) {
                activePlayers++;
            }
        }

        if (activePlayers <= 1) {
            game.finishGame();
        }
    }
}
