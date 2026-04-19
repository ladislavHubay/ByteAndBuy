package org.hubay.byteandbuy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Obsahuje informacie potrebne pre frontend.
 */
@Getter
public class TurnResponse {
    @Setter
    private String currentPlayer;
    @Setter
    private int dice;
    @Setter
    private int fromPosition;
    @Setter
    private int toPosition;
    @Setter
    private String tileName;
    @Setter
    private int money;
    @Setter
    private boolean extraTurn;
    @Setter
    private String nextPlayer;
    @Setter
    private List<String> events = new ArrayList<>();
}
