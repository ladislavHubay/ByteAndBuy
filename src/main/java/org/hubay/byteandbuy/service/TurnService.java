package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.springframework.stereotype.Service;

/**
 * Riesi riadenie priebehu tahov hracov.
 */
@Service
public class TurnService {
    private final PlayerStateService playerStateService;

    public TurnService(PlayerStateService playerStateService) {
        this.playerStateService = playerStateService;
    }

    /**
     * Ukonci tah aktualneho hraca. Ak hrac hodi 6, na tahu zostava rovnaky hrac.
     * Inak posunie dalsieho hraca na tah.
     */
    public void finishTurn(Game game) {
        Player player = game.getCurrentPlayer();

        if(game.isWaitingForDecision()){
            return;
        }

        if (shouldEndTurn(game, player)) {
            game.getEventCollector().add(player.getName() + " ma na ucte: " + player.getMoney());
            moveToNextPlayer(game);
        } else {
            game.getEventCollector().add(player.getName() + " hodil si 6. Hadzes znova");
        }
    }

    /**
     * Urcuje ci sa ma tah hraca ukoncit.
     * (hrac hodil ine ako 6, hrac je vo vazani, hrac je vyradeny z hry - nastaveny ako neaktivny)
     */
    public boolean shouldEndTurn(Game game, Player player) {
        return game.getLastDice() != 6
                || player.isInJail()
                || !player.isInGame();
    }

    /**
     * Presunie hru na dalsieho aktivneho hraca v poradi.
     */
    public void moveToNextPlayer(Game game) {
        playerStateService.checkGameFinished(game);

        if (game.isFinished()) {
            return;
        }

        int nextPlayer = game.getCurrentPlayerIndex();

        do {
            nextPlayer = (nextPlayer + 1) % game.getPlayers().size();
        } while (!game.getPlayers().get(nextPlayer).isInGame());

        game.setCurrentPlayerIndex(nextPlayer);
    }
}
