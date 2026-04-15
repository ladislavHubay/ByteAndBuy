package model.tiles;

import model.game.Game;
import model.player.Player;

// jedno policko na hracej doske. Spravanie policka urcuje trieda PropertyTile.
public abstract class Tile {
    // pozicia policka v List<Tile> tiles;
    private final int position;
    // nazov policka(napr. "start", "nazov firmy", "finance", "nahoda", ...)
    private final String name;

    public Tile(int position, String name) {
        this.position = position;
        this.name = name;
    }

    // metoda na nakup policka hracom.
    // - len urcuje ze sa ma metoda vykonat. Samotne spravanie metody urcuje Trieda ktora dedi tuto triedu.
    public void interact(Game game, Player player) {}

    public void onPlayerBankrupt(Player player) {}

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}
