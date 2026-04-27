package org.hubay.byteandbuy.dto;

import lombok.Getter;
import lombok.Setter;
import org.hubay.byteandbuy.model.tiles.TileResult;

import java.util.List;

/**
 * Obsahuje informacie potrebne pre frontend.
 */
@Getter
@Setter
public class TurnResponse {
    private String currentPlayer;
    private int dice;
    private int fromPosition;
    private int toPosition;
    private String tileName;
    private List<PlayerSummary> players;
    private List<String> events;
    private TileResult action;
    private String card;
}
