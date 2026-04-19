package org.hubay.byteandbuy.model.board;

import lombok.Getter;
import org.hubay.byteandbuy.model.tiles.Tile;

import java.util.List;

/**
 * Hracia doska. Obsahuje zoznam vsetkych policok.
 */
@Getter
public class Board {
    private final List<Tile> tiles;
    private final Tile startTile;

    public Board(List<Tile> tiles, Tile startTile) {
        this.tiles = tiles;
        this.startTile = startTile;
    }
}
