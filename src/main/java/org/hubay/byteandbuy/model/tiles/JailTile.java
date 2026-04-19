package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Policko vazanie.
 */
public class JailTile extends Tile {
    public JailTile(int position, String name) {
        super(position, name);
    }

    /**
     * Policko samotne nema ziadny efekt.
     * Sluzi len na urcenie miesta kam sa hrac posunie a ostane
     * na tejto pozicii podla hernych pravidiel pri potiahnuti karty.
     */
    @Override
    public TileResult interact(Game game, Player player) {
        return TileResult.CONTINUE;
    }
}
