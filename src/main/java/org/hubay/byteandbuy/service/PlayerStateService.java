package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.BankruptAware;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.springframework.stereotype.Service;

// Riesi stav hraca (Bankrot, vypnutie hraca, kontrola ci su minimalne dvaja hraci v hre)
@Service
public class PlayerStateService {
    // skontroluje ci hrac ma na ucte peniaze,
    // ak nie vyradi ho z hry a jeho kupene policka nastavi na znovu kupitelne.
    // V pripade ak ostane v hre iba jeden hrac, hra skonci.
    public void checkBankruptcy(Game game, Player player) {
        if (player.getMoney() <= 0) {
            game.getEventCollector().add(player.getName() + " skrachoval/-la");
            removePlayerFromGame(game, player);
        }
    }

    // Vypne daneho hraca v hre, jeho kupene policka nastavi na znovuputitelne,
    // skontroluje ci ostalo dostatok hracov v hre alebo sa hra uplne ukonci.
    public void removePlayerFromGame(Game game, Player player) {
        player.setInGame(false);

        for (Tile tile : game.getBoard().getTiles()) {
            if (tile instanceof BankruptAware bankruptAware) {
                bankruptAware.onPlayerBankrupt(player);
            }
        }

        checkGameFinished(game);
    }

    // Kontroluje ci su v hre minimalne dvaja hraci.
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
