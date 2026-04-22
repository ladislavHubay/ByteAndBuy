package org.hubay.byteandbuy.dto;

import lombok.Getter;
import lombok.Setter;
import org.hubay.byteandbuy.model.tiles.TileResult;

import java.util.ArrayList;
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
    private int money;
    private boolean extraTurn;
    private String nextPlayer;
    private List<String> events = new ArrayList<>();
    private TileResult action;
    private String card;
}
