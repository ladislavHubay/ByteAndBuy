package org.hubay.byteandbuy.model.game;

import lombok.Getter;
import lombok.Setter;
import org.hubay.byteandbuy.config.GameConfig;
import org.hubay.byteandbuy.event.GameEventCollector;
import org.hubay.byteandbuy.model.board.Board;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Tile;

import java.util.List;

/**
 * Reprezentuje aktualny stav prebiehajucej hry.
 */
@Getter
public class Game {
    @Setter
    private transient GameEventCollector eventCollector;
    private final GameConfig gameConfig;
    private final List<Player> players;
    private final Board board;
    @Setter
    private int currentPlayerIndex;
    private int lastDice;
    private GameState state;

    public Game(GameConfig gameConfig, List<Player> players, Board board, int currentPlayerIndex) {
        this.gameConfig = gameConfig;
        this.players = players;
        this.board = board;
        this.currentPlayerIndex = currentPlayerIndex;
        this.state = GameState.PLAYING;
    }

    /**
     * Vrati hraca, ktory je prave na tahu.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Nastavi hodnotu posledneho hodu kockou.
     */
    public void setDice(int dice) {
        this.lastDice = dice;
    }

    /**
     * Vrati stav hry - cakanie na rozhodnutie hraca kupit/nekupit policko.
     * Moze ist napriklad o situaciu ak je hrac na policku ktore je mozne kupit
     * a hra caka na rozhodnutie hraca ci policko kupi alebo nie.
     */
    public boolean isWaitingForBuy() {
        return state == GameState.WAITING_FOR_BUY;
    }

    /**
     * Vrati stav hry - cakanie na potiahnutu kartu.
     * Moze ist napriklad o situaciu ak je hrac na policku kde sa taha karta
     * a hra caka na potiahnutie karty..
     */
    public boolean isWaitingForCard() {
        return state == GameState.WAITING_FOR_CARD;
    }

    /**
     * Vrati ci hra momentalne caka na akciu hraca.
     * Pouziva sa pri spolocnom spracovani stavov, v ktorych tah este nema skoncit.
     */
    public boolean isWaitingForAction() {
        return isWaitingForBuy() || isWaitingForCard();
    }

    /**
     * Vrati stav hry - hra sa skoncila.
     * Napriklad ak v hre ostal iba jeden (posledny) hrac.
     */
    public boolean isFinished() {
        return state == GameState.FINISHED;
    }

    /**
     * Nastavy stav hry - cakanie na rozhodnutie pri moznosti nakupu.
     * Moze ist napriklad o situaciu ak je hrac na policku ktore je mozne kupit
     * a hra caka na rozhodnutie hraca ci policko kupi alebo nie.
     */
    public void waitForBuy() {
        this.state = GameState.WAITING_FOR_BUY;
    }

    /**
     * Nastavy stav hry - cakanie na rozhodnutie pri moznosti tahania karty.
     * Moze ist napriklad o situaciu ak je hrac na policku kde sa taha karta
     * a hra caka na potiahnutie karty.
     */
    public void waitForCard() {
        this.state = GameState.WAITING_FOR_CARD;
    }

    /**
     * Nastavy stav hry - pokracovat v hre.
     * Napriklad po rozhodnuti hraca kupit alebo nekupit policko hra moze pokracovat dalej.
     */
    public void resumePlaying() {
        this.state = GameState.PLAYING;
    }

    /**
     * Nastavy stav hry - hra skoncila.
     * Napriklad ak v hre ostal iba jeden (posledny) hrac.
     */
    public void finishGame() {
        this.state = GameState.FINISHED;
    }

    /**
     * Vrati policko na ktorom prave stoji hrac.
     */
    public Tile getCurrentTile(Player player) {
        return board.getTiles().get(player.getPosition());
    }

    /**
     * Vrati pocet policok na hracej doske.
     */
    public int getBoardSize() {
        return board.getTiles().size();
    }

    /**
     * Posunie hraca o urcity pocet krokov.
     * Hrcovy pripise bonus na ucet za START pokial su splnene podmienky.
     */
    public void movePlayer(Player player, int steps, boolean applyStartBonus) {
        int oldPosition = player.getPosition();
        int newPosition = player.move(steps, getBoardSize());
        applyStartBonusIfPassed(player, oldPosition, newPosition, applyStartBonus);
    }

    /**
     * Posunie hraca na konkretne policko.
     * Hrcovy pripise bonus na ucet za START pokial su splnene podmienky.
     */
    public void movePlayerTo(Player player, int position, boolean applyStartBonus) {
        int oldPosition = player.getPosition();
        int newPosition = player.moveTo(position);
        applyStartBonusIfPassed(player, oldPosition, newPosition, applyStartBonus);
    }

    /**
     * Kontroluje ci hrac presiel cez START alebo sa postavil na policko START.
     * V pripade naroku na bonus podla pravidiel,
     * tento bonus za START pripise na ucet hraca.
     */
    private void applyStartBonusIfPassed(Player player, int oldPosition, int newPosition, boolean applyStartBonus) {
        boolean passedStart = newPosition < oldPosition;

        if (passedStart && applyStartBonus) {
            player.receive(gameConfig.getStartBonus());
            eventCollector.add(player.getName() + " ziskal " + gameConfig.getStartBonus() + " za START");
        }
    }
}
