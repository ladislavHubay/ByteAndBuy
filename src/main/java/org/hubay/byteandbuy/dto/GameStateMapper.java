package org.hubay.byteandbuy.dto;

import lombok.NoArgsConstructor;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Buyable;
import org.hubay.byteandbuy.model.tiles.PropertyGroup;
import org.hubay.byteandbuy.model.tiles.PropertyTile;
import org.hubay.byteandbuy.model.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper prevadza model hry na DTO urcene pre frontend cez WebSocket.
 */
@NoArgsConstructor
public class GameStateMapper {

    /**
     * Prevedie aktualny stav hry na DTO.
     */
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
                companies(game),
                availableActions(game)
        );
    }

    /**
     * Vrati aktualneho hraca v podobe DTO.
     */
    private static PlayerSummary currentPlayer(Game game) {
        if (game.getPlayers().isEmpty()) {
            return null;
        }

        return PlayerSummaryMapper.toSummary(game.getCurrentPlayer());
    }

    /**
     * Vrati zoznam vsetkych policok v podobe DTO.
     */
    private static BoardStateDto board(Game game) {
        List<TileStateDto> tiles = new ArrayList<>();

        for (Tile tile : game.getBoard().getTiles()) {
            tiles.add(tile(tile));
        }

        return new BoardStateDto(tiles);
    }

    /**
     * Vrati zoznam vsetkych firiem v podobe DTO.
     */
    private static List<CompanyStateDto> companies(Game game) {
        List<CompanyStateDto> companies = new ArrayList<>();

        for (PropertyGroup company : game.getCompanies().getGroups()) {
            companies.add(company(company));
        }

        return companies;
    }

    /**
     * Vrati jednu firmu v podobe DTO.
     */
    private static CompanyStateDto company(PropertyGroup company) {
        List<Integer> propertyPositions = new ArrayList<>();

        for (PropertyTile property : company.getProperties()) {
            propertyPositions.add(property.getPosition());
        }

        return new CompanyStateDto(
                company.getName(),
                company.getCurrentPrice(),
                propertyPositions
        );
    }

    /**
     * Vrati jedno policko z hracej dosky v podobe DTO.
     */
    private static TileStateDto tile(Tile tile) {
        Integer price = null;
        Integer rent = null;
        PlayerSummary owner = null;
        boolean buyable = tile instanceof Buyable;

        if (tile instanceof Buyable buyableTile) {
            price = buyableTile.getPrice();
            Player ownerPlayer = buyableTile.getOwner();

            if(tile instanceof PropertyTile propertyTile) {
                rent = propertyTile.getRent();
            }

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
                owner,
                rent
        );
    }

    /**
     * Vrati zoznam akcii ktore moze hrac vykonat podla aktualneho stavu hry.
     */
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
