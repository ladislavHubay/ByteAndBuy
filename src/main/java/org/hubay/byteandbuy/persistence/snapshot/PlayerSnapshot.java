package org.hubay.byteandbuy.persistence.snapshot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Reprezentuje ulozeny stav hraca.
 */
@Getter
@Setter
@NoArgsConstructor
public class PlayerSnapshot {
    private UUID id;
    private String name;
    private int position;
    private int money;
    private boolean inGame;
    private boolean inJail;

    public PlayerSnapshot(UUID id, String name, int position, int money, boolean inGame, boolean inJail) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.money = money;
        this.inGame = inGame;
        this.inJail = inJail;
    }
}
