package org.hubay.byteandbuy.model.player;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Reprezentuje hraca.
 */
@Getter
public class Player {
    private final UUID id;
    private final String name;
    private int position;
    private int money;
    @Setter
    private boolean inGame;
    @Setter
    @Getter
    private boolean inJail;

    public Player(UUID id, String name, int position, int money, boolean inGame) {
        this(id, name, position, money, inGame, false);
    }

    public Player(UUID id, String name, int position, int money, boolean inGame, boolean inJail) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.money = money;
        this.inGame = inGame;
        this.inJail = inJail;
    }

    /**
     * Skontroluje ci ma hrac dostatocne financne prostriedky na nakup.
     */
    public boolean canBuy(int price) {
        return this.money > price;
    }

    /**
     * Odpocita hracovy danu sumu z uctu.
     */
    public void pay(int amount) {
        this.money -= amount;
    }

    /**
     * Prida hracovy danu sumu na ucet.
     */
    public void receive(int amount) {
        this.money += amount;
    }

    /**
     * posuva hraca o presnu pocet krokov.
     * Zohladnuje pocet policok na hracej doske.
     */
    public int move(int steps, int boardSize) {
        int newPosition = (this.position + steps) % boardSize;
        this.position = newPosition;
        return newPosition;
    }

    /**
     * Premiestni hraca na konkretnu poziciu.
     */
    public int moveTo(int position) {
        this.position = position;
        return position;
    }
}
