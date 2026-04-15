package model.board;

import model.tiles.Tile;

import java.util.List;

// Hracia doska (zatial len policka).
public class Board {
    // zoznam hracich policok.
    private final List<Tile> tiles;
    // policko start
    private final Tile startTile;

    public Board(List<Tile> tiles, Tile startTile) {
        this.tiles = tiles;
        this.startTile = startTile;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile getStartTile() {
        return startTile;
    }
}
