package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

/**
 * Abstraktna tireda reprezentuje jedno policko na hracej doske.
 * Implementaciu ma kazdy typ policka samostatne.
 */
@Getter
public abstract class Tile {
    private final int position;
    private final String name;
    private final TileType type;

    public Tile(int position, String name, TileType type) {
        this.position = position;
        this.name = name;
        this.type = type;
    }

    /**
     * Definuje spravanie policka. Vracia vysledok interakcie.
     */
    public abstract TileActionType interact(Game game, Player player);
}
