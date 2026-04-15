package model.tiles;

import model.game.Game;
import model.player.Player;

// Urcuje spravanie konkretneho policka - iba policka START.
public class StartTile extends Tile {
    // bonus co sa pripise na ucet hraca
    private final int bonus;

    public StartTile(int position, String name, int bonus) {
        super(position, name);
        this.bonus = bonus;
    }

    @Override
    public void interact(Game game, Player player) {
        player.setMoney(player.getMoney() + bonus);
        System.out.println(player.getName() + " získal bonus " + bonus + " za START");
    }
}
