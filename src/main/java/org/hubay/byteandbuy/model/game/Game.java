package org.hubay.byteandbuy.model.game;

import lombok.Getter;
import lombok.Setter;
import org.hubay.byteandbuy.config.GameConfig;
import org.hubay.byteandbuy.model.board.Board;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Tile;

import java.util.List;

// konkrétna bežiaca hra v danom momente.
@Getter
public class Game {
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

    public boolean movePlayer(Player player, int steps) {
        boolean passedStart = player.move(steps, getBoardSize());

        if (passedStart) {
            System.out.println("BONUS TRIGGER - movePlayer");
            player.receive(gameConfig.getStartBonus());
        }

        return passedStart;
    }

    public boolean movePlayerTo(Player player, int position) {
        boolean passedStart = player.moveTo(position);

        if (passedStart) {
            System.out.println("BONUS TRIGGER - movePlayerTo");
            player.receive(gameConfig.getStartBonus());
        }

        return passedStart;
    }
}
