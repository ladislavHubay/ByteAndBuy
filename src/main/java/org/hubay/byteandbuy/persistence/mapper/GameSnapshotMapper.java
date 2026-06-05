package org.hubay.byteandbuy.persistence.mapper;

import org.hubay.byteandbuy.model.board.Board;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Buyable;
import org.hubay.byteandbuy.model.tiles.PropertyGroup;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.hubay.byteandbuy.persistence.snapshot.CompanySnapshot;
import org.hubay.byteandbuy.persistence.snapshot.GameSnapshot;
import org.hubay.byteandbuy.persistence.snapshot.PlayerSnapshot;
import org.hubay.byteandbuy.persistence.snapshot.TileOwnershipSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Prevadza Game na GameSnapshot a GameSnapshot na Game.
 */
public class GameSnapshotMapper {
    /**
     * Vytvori snapshot celej hry.
     */
    public static GameSnapshot toSnapshot(Game game) {
        List<PlayerSnapshot> players = new ArrayList<>();
        List<TileOwnershipSnapshot> tileOwnerships = new ArrayList<>();
        List<CompanySnapshot> companyPrices = new ArrayList<>();

        for (Player player : game.getPlayers()) {
            players.add(toSnapshot(player));
        }

        collectTileOwnerships(game, tileOwnerships);
        collectCompanyPrices(game, companyPrices);

        return new GameSnapshot(
                game.getCurrentPlayerIndex(),
                game.getLastDice(),
                game.getState(),
                players,
                tileOwnerships,
                companyPrices,
                game.getRandomDeck().toSnapshot(),
                game.getFinanceDeck().toSnapshot()
        );
    }

    /**
     * Prevedie Player ba PlayerSnapshot.
     */
    private static PlayerSnapshot toSnapshot(Player player) {
        return new PlayerSnapshot(
                player.getId(),
                player.getName(),
                player.getPosition(),
                player.getMoney(),
                player.isInGame(),
                player.isInJail()
        );
    }

    /**
     * Obnovi stav ulozenej hry z DB.
     */
    public static void applySnapshot(Game game, GameSnapshot snapshot) {
        game.setCurrentPlayerIndex(snapshot.getCurrentPlayerIndex());
        game.setDice(snapshot.getLastDice());
        game.setState(snapshot.getState());
        applyTileOwnerships(game, snapshot.getTileOwnerships());
        applyCompanyPrices(game, snapshot.getCompanyPrices());
    }

    /**
     * Prejde vsetky policka (policka ktore mozu mat ownera) a ulozi ich.
     */
    private static void collectTileOwnerships(Game game, List<TileOwnershipSnapshot> tileOwnerships) {
        for (Tile tile : game.getBoard().getTiles()) {
            if (tile instanceof Buyable buyable && buyable.getOwner() != null) {
                tileOwnerships.add(new TileOwnershipSnapshot(tile.getPosition(), buyable.getOwner().getId()));
            }
        }
    }

    /**
     * Ulozi aktualne trhove ceny firiem.
     */
    private static void collectCompanyPrices(Game game, List<CompanySnapshot> companySnapshot) {
        for (PropertyGroup company : game.getCompanies().getGroups()) {
            companySnapshot.add(new CompanySnapshot(company.getName(), company.getCurrentPrice(), company.getInitialPrice()));
        }
    }

    /**
     * Onnovi vlastnictvo policok z DB (snapshotu).
     */
    private static void applyTileOwnerships(Game game, List<TileOwnershipSnapshot> snapshots) {
        Board board = game.getBoard();
        clearTileOwnerships(board);

        if (snapshots == null) {
            return;
        }

        for (TileOwnershipSnapshot snapshot : snapshots) {
            Tile tile = board.getTiles().get(snapshot.getTilePosition());

            if (tile instanceof Buyable buyable) {
                Player owner = resolveOwner(game, snapshot);
                buyable.setOwner(owner);
            }
        }
    }

    /**
     * Obnovi trhove ceny firiem. Ak starsi snapshot ceny neobsahuje, ostanu default hodnoty.
     */
    private static void applyCompanyPrices(Game game, List<CompanySnapshot> snapshots) {
        if (snapshots == null) {
            return;
        }

        for (CompanySnapshot snapshot : snapshots) {
            PropertyGroup company = resolveCompany(game, snapshot.getCompanyName());

            if (company != null) {
                company.setCurrentPrice(snapshot.getCurrentPrice());
                company.setInitialPrice(snapshot.getInitialPrice());
            }
        }
    }

    /**
     * Najde firmu podla mena ulozeneho v snapshote.
     */
    private static PropertyGroup resolveCompany(Game game, String companyName) {
        for (PropertyGroup company : game.getCompanies().getGroups()) {
            if (company.getName().equals(companyName)) {
                return company;
            }
        }

        return null;
    }

    /**
     * Najde a vrati hraca podla ID ulozeneho v snapshote.
     */
    private static Player resolveOwner(Game game, TileOwnershipSnapshot snapshot) {
        return game.getPlayerById(snapshot.getOwnerPlayerId());
    }

    /**
     * Resetuje vlastnictvo poli ktore je mozne vlastnit (owner nastavi na null).
     */
    private static void clearTileOwnerships(Board board) {
        for (Tile tile : board.getTiles()) {
            if (tile instanceof Buyable buyable) {
                buyable.setOwner(null);
            }
        }
    }
}
