package org.hubay.byteandbuy.model.board;

import lombok.Getter;
import org.hubay.byteandbuy.model.tiles.Tile;

import java.util.List;

// Hracia doska (zatial len policka).
@Getter
public class Board {
    // zoznam hracich policok.
    private final List<Tile> tiles;
    // policko start
    private final Tile startTile;

    public Board(List<Tile> tiles, Tile startTile) {
        this.tiles = tiles;
        this.startTile = startTile;
    }
}
