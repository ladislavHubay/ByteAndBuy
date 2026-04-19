package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.PropertyTile;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.hubay.byteandbuy.model.tiles.TileResult;
import org.springframework.stereotype.Service;

/**
 * Vyhodnocuje efekty policok.
 */
@Service
public class TileActionService {
    private final PlayerStateService playerStateService;

    public TileActionService(PlayerStateService playerStateService) {
        this.playerStateService = playerStateService;
    }

    /**
     * Vyhodnocuje efekt policka na ktorom hrac stoji.
     * Podporuje retazenie efektov (policko -> tahanie karty -> posun na dalsie policko...)
     */
    public void resolveTileEffects(Game game, Player player) {
        int previousPosition;

        do {
            previousPosition = player.getPosition();

            TileResult result = applyTileEffect(game, player);

            if (TileResult.WAIT_FOR_DECISION == result) {
                game.waitForDecision();
            }

            playerStateService.checkBankruptcy(game, player);

            if (game.isWaitingForDecision()) {
                break;
            }

        } while (player.getPosition() != previousPosition);
    }

    /**
     * Aplikuje efekt konkretneho policka.
     */
    private TileResult applyTileEffect(Game game, Player player) {
        Tile tile = game.getCurrentTile(player);

        if (tile instanceof PropertyTile property && property.getOwner() != null) {
            game.getEventCollector().add(player.getName() + " sa posunul na " +
                    tile.getName() + " vlastni ho " + property.getOwner().getName());
        } else {
            game.getEventCollector().add(player.getName() + " sa posunul na " + tile.getName());
        }

        return tile.interact(game, player);
    }
}
