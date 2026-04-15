package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// jedno policko na hracej doske. Spravanie policka urcuje trieda PropertyTile.
@Getter
public abstract class Tile {
    // pozicia policka v List<Tile> tiles;
    private final int position;
    // nazov policka(napr. "start", "nazov firmy", "finance", "nahoda", ...)
    private final String name;

    public Tile(int position, String name) {
        this.position = position;
        this.name = name;
    }

    // Metoda urcuje interakciu - spravanie policka ked je na nom hrac.
    public abstract TileResult interact(Game game, Player player);
}
