package org.hubay.byteandbuy.model.player;

import lombok.Getter;
import lombok.Setter;

// hrac
@Getter
public class Player {
    // Nick hraca.
    private final String name;
    // aktualna pozicia na hracej doske (Board - index v List<Tile> tiles;).
    private int position;
    // aktualna suma penazi ktore hrac ma.
    private int money;
    // ci je hrac v hre alebo skoncil.
    @Setter
    private boolean inGame;
    // ci je hrac vo vazani alebo nie.
    @Setter
    @Getter
    private boolean inJail;

    public Player(String name, int position, int money, boolean inGame) {
        this.name = name;
        this.position = position;
        this.money = money;
        this.inGame = inGame;
    }

    public boolean canAfford(int amount) {
        return this.money >= amount;
    }

    public void pay(int amount) {
        this.money -= amount;
    }

    public void receive(int amount) {
        this.money += amount;
    }

    public boolean move(int steps, int boardSize) {
        int oldPosition = this.position;
        int newPosition = (this.position + steps) % boardSize;

        this.position = newPosition;

        return didPassStart(oldPosition, newPosition);
    }

    public boolean moveTo(int position) {
        int oldPosition = this.position;
        this.position = position;

        return didPassStart(oldPosition, position);
    }

    private boolean didPassStart(int oldPosition, int newPosition) {
        return newPosition < oldPosition;
    }
}
