package org.hubay.byteandbuy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Obsahuje informacie o hracovy potrebne pre frontend.
 */
@Getter
@Setter
public class PlayerSummary {
    private UUID id;
    private String name;
    private int money;
    private int position;
    private boolean inGame;
    private boolean inJail;
}
