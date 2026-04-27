package org.hubay.byteandbuy.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Obsahuje informacie o hracovy potrebne pre frontend.
 */
@Getter
@Setter
public class PlayerSummary {
    private String name;
    private int money;
    private int position;
    private boolean inGame;
    private boolean inJail;
}
