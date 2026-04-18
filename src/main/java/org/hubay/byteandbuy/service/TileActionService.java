package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.PropertyTile;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.hubay.byteandbuy.model.tiles.TileResult;
import org.springframework.stereotype.Service;

// Riesi efekti na policku (co sa ma stat ak hrac stoji na danom policku)
@Service
public class TileActionService {
    private final PlayerStateService playerStateService;

    public TileActionService(PlayerStateService playerStateService) {
        this.playerStateService = playerStateService;
    }

    // Vykona efekt policka aj ked sa hrac posunie (napriklad kvoli potiahnutej karte).
    // Opakuje az do dovtedy kym nezisti ze sa po vykonani policka hrac nepohol z miesta.
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
