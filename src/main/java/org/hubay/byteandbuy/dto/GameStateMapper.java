package org.hubay.byteandbuy.dto;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Buyable;
import org.hubay.byteandbuy.model.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class GameStateMapper {
    private GameStateMapper() {
    }

    public static GameStateDto toDto(Game game) {
        List<PlayerSummary> players = PlayerSummaryMapper.toSummaries(game.getPlayers());

        return new GameStateDto(
                game.getState(),
                game.isWaitingForPlayers(),
                game.isPlaying(),
                game.isWaitingForBuy(),
                game.isWaitingForCard(),
                game.isFinished(),
                game.getCurrentPlayerIndex(),
                currentPlayer(game),
                game.getLastDice(),
                players,
                board(game),
                availableActions(game)
        );
    }

    private static PlayerSummary currentPlayer(Game game) {
        if (game.getPlayers().isEmpty()) {
            return null;
        }

        return PlayerSummaryMapper.toSummary(game.getCurrentPlayer());
    }

    private static BoardStateDto board(Game game) {
        List<TileStateDto> tiles = new ArrayList<>();

        for (Tile tile : game.getBoard().getTiles()) {
            tiles.add(tile(tile));
        }

        return new BoardStateDto(tiles);
    }

    private static TileStateDto tile(Tile tile) {
        Integer price = null;
        PlayerSummary owner = null;
        boolean buyable = tile instanceof Buyable;

        if (tile instanceof Buyable buyableTile) {
            price = buyableTile.getPrice();
            Player ownerPlayer = buyableTile.getOwner();

            if (ownerPlayer != null) {
                owner = PlayerSummaryMapper.toSummary(ownerPlayer);
            }
        }

        return new TileStateDto(
                tile.getPosition(),
                tile.getName(),
                tile.getType().name(),
                buyable,
                price,
                owner
        );
    }

    private static List<String> availableActions(Game game) {
        if (game.isFinished() || game.isWaitingForPlayers()) {
            return List.of();
        }
        if (game.isPlaying()) {
            return List.of("ROLL");
        }
        if (game.isWaitingForBuy()) {
            return List.of("BUY_PROPERTY", "SKIP_PURCHASE");
        }
        if (game.isWaitingForCard()) {
            return List.of("DRAW_CARD");
        }

        return List.of();
    }
}
