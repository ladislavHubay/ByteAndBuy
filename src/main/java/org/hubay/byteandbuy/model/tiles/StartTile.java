package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Policko START na hracej doske.
 */
public class StartTile extends Tile {
    public StartTile(int position, String name) {
        super(position, name);
    }

    /**
     * Policko sam o sebe nevykonva ziadnu akciu.
     */
    @Override
    public TileResult interact(Game game, Player player) {
        return TileResult.CONTINUE;
    }
}
