package org.hubay.byteandbuy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Request pre akciu konkretneho hraca.
 */
@Getter
@Setter
public class PlayerActionRequest {
    private UUID playerId;
}
