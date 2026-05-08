package org.hubay.byteandbuy.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Request na pripojenie hraca do hry.
 */
@Getter
@Setter
public class JoinGameRequest {
    private String playerName;
}
