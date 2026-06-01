package org.hubay.byteandbuy.dto;

import lombok.Getter;
import org.hubay.byteandbuy.model.game.GameState;

import java.util.List;

@Getter
public class GameStateDto {
    private final GameState state;
    private final boolean waitingForPlayers;
    private final boolean playing;
    private final boolean waitingForBuy;
    private final boolean waitingForCard;
    private final boolean finished;
    private final int currentPlayerIndex;
    private final PlayerSummary currentPlayer;
    private final int lastDice;
    private final List<PlayerSummary> players;
    private final BoardStateDto board;
    private final List<String> availableActions;

    public GameStateDto(GameState state, boolean waitingForPlayers, boolean playing,
                        boolean waitingForBuy, boolean waitingForCard, boolean finished,
                        int currentPlayerIndex, PlayerSummary currentPlayer, int lastDice,
                        List<PlayerSummary> players, BoardStateDto board, List<String> availableActions) {
        this.state = state;
        this.waitingForPlayers = waitingForPlayers;
        this.playing = playing;
        this.waitingForBuy = waitingForBuy;
        this.waitingForCard = waitingForCard;
        this.finished = finished;
        this.currentPlayerIndex = currentPlayerIndex;
        this.currentPlayer = currentPlayer;
        this.lastDice = lastDice;
        this.players = players;
        this.board = board;
        this.availableActions = availableActions;
    }
}
