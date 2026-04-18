package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.springframework.stereotype.Service;

@Service
public class TurnService {
    private final PlayerStateService playerStateService;

    public TurnService(PlayerStateService playerStateService) {
        this.playerStateService = playerStateService;
    }

    // Zabespeci kontrolu ci je na tahu znovu aktualny hrac (napriklad hodil 6) alebo sa ukonci tah aktualneho hraca,
    public void finishTurn(Game game) {
        Player player = game.getCurrentPlayer();

        if(game.isWaitingForDecision()){
            return;
        }

        if (shouldEndTurn(game, player)) {
            game.getEventCollector().add(player.getName() + " ma na ucte: " + player.getMoney());
            moveToNextPlayer(game);
        } else {
            game.getEventCollector().add(player.getName() + " hodil 6 → hrá znova");
        }
    }

    public boolean shouldEndTurn(Game game, Player player) {
        return game.getLastDice() != 6
                || player.isInJail()
                || !player.isInGame();
    }

    // ukonci tah hraca a posunie tah na dalsieho hraca.
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
