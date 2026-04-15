package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Policko vazanie.
public class JailTile extends Tile {
    public JailTile(int position, String name) {
        super(position, name);
    }

    @Override
    public TileResult interact(Game game, Player player) {
        String message = player.getName() + " je vo väzení";
        return TileResult.simple(message);
    }
}
