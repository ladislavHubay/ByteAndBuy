package model.game;

import model.board.Board;
import model.player.Player;

import java.util.List;

// konkrétna bežiaca hra v danom momente.
public class Game {
    // zoznam hracov - indexy 0 az N urcuje poradie hracaov.
    private final List<Player> players;
    // hracia doska.
    private final Board board;
    // je to index do List<Player> players, ktorý určuje aktuálneho hráča
    private int currentPlayerIndex;
    // hra bezi alebo sa ukoncila.
    private boolean finished;

    public Game(List<Player> players, Board board, int currentPlayerIndex, boolean finished) {
        this.players = players;
        this.board = board;
        this.currentPlayerIndex = currentPlayerIndex;
        this.finished = finished;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
