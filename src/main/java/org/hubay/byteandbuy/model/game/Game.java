package org.hubay.byteandbuy.model.game;

import lombok.Getter;
import lombok.Setter;
import org.hubay.byteandbuy.config.GameConfig;
import org.hubay.byteandbuy.event.GameEventCollector;
import org.hubay.byteandbuy.model.board.Board;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Tile;

import java.util.List;

// konkrétna bežiaca hra v danom momente.
@Getter
public class Game {
    @Setter
    private transient GameEventCollector eventCollector;
    // Pravidla hry (bonus za start, multyplikatory,....)
    private final GameConfig gameConfig;
    // zoznam hracov - indexy 0 az N urcuje poradie hracaov.
    private final List<Player> players;
    // hracia doska.
    private final Board board;
    // je to index do List<Player> players, ktorý určuje aktuálneho hráča
    @Setter
    private int currentPlayerIndex;
    // hod kockou
    private int lastDice;
    // Stav hraca
    private GameState state;

    public Game(GameConfig gameConfig, List<Player> players, Board board, int currentPlayerIndex) {
        this.gameConfig = gameConfig;
        this.players = players;
        this.board = board;
        this.currentPlayerIndex = currentPlayerIndex;
        this.state = GameState.PLAYING;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setDice(int dice) {
        this.lastDice = dice;
    }

    public boolean isWaitingForDecision() {
        return state == GameState.WAITING_FOR_DECISION;
    }

    public boolean isFinished() {
        return state == GameState.FINISHED;
    }

    public void waitForDecision() {
        this.state = GameState.WAITING_FOR_DECISION;
    }

    public void resumePlaying() {
        this.state = GameState.PLAYING;
    }

    public void finishGame() {
        this.state = GameState.FINISHED;
    }

    public Tile getCurrentTile(Player player) {
        return board.getTiles().get(player.getPosition());
    }

    public int getBoardSize() {
        return board.getTiles().size();
    }

    public void movePlayer(Player player, int steps, boolean applyStartBonus) {
        int oldPosition = player.getPosition();
        int newPosition = player.move(steps, getBoardSize());

        boolean passedStart = newPosition < oldPosition;

        if (passedStart && applyStartBonus) {
            player.receive(gameConfig.getStartBonus());
            eventCollector.add(player.getName() + " ziskal bonus za START");
        }
    }

    public void movePlayerTo(Player player, int position, boolean applyStartBonus) {
        int oldPosition = player.getPosition();
        int newPosition = player.moveTo(position);

        boolean passedStart = newPosition < oldPosition;

        if (passedStart && applyStartBonus) {
            player.receive(gameConfig.getStartBonus());
            eventCollector.add(player.getName() + " ziskal bonus za START");
        }
    }
}
