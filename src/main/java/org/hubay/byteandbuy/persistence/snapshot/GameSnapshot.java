package org.hubay.byteandbuy.persistence.snapshot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hubay.byteandbuy.model.game.GameState;

import java.util.List;

/**
 * Reprezentuje ulozeny stav hry.
 */
@Getter
@Setter
@NoArgsConstructor
public class GameSnapshot {
    private int currentPlayerIndex;
    private int lastDice;
    private GameState state;
    private List<PlayerSnapshot> players;
    private List<TileOwnershipSnapshot> tileOwnerships;

    public GameSnapshot(int currentPlayerIndex, int lastDice, GameState state,
                        List<PlayerSnapshot> players, List<TileOwnershipSnapshot> tileOwnerships) {
        this.currentPlayerIndex = currentPlayerIndex;
        this.lastDice = lastDice;
        this.state = state;
        this.players = players;
        this.tileOwnerships = tileOwnerships;
    }
}
