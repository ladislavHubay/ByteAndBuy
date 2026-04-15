package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Urcuje spravanie konkretneho policka - iba policka START.
public class StartTile extends Tile {
    public StartTile(int position, String name) {
        super(position, name);
    }

    @Override
    public TileResult interact(Game game, Player player) {
        String message = player.getName() + " získal bonus " + game.getGameConfig().getStartBonus() + " za START - stoji na START";

        return TileResult.simple(message);
    }
}
