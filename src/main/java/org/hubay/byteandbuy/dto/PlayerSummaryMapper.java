package org.hubay.byteandbuy.dto;

import org.hubay.byteandbuy.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Trieda na pripravu DTO pre frontend z Player.
 */
public class PlayerSummaryMapper {
    /**
     * Prevedie jeden objekt Player na DTO objekt PlayerSumary.
     */
    public static PlayerSummary toSummary(Player player) {
        PlayerSummary summary = new PlayerSummary();
        summary.setId(player.getId());
        summary.setName(player.getName());
        summary.setMoney(player.getMoney());
        summary.setPosition(player.getPosition());
        summary.setInGame(player.isInGame());
        summary.setInJail(player.isInJail());
        return summary;
    }

    /**
     * Prevedie zoznam vsetkych Player objektov na zoznam PlayerSummary DTO.
     */
    public static List<PlayerSummary> toSummaries(List<Player> players) {
        List<PlayerSummary> result = new ArrayList<>();

        for (Player player : players) {
            result.add(toSummary(player));
        }

        return result;
    }
}
