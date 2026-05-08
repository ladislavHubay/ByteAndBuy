package org.hubay.byteandbuy.persistence.snapshot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Reprezentuje ulozenho vlastnika policka (iba policka ktore je mozne vlastnit).
 */
@Getter
@Setter
@NoArgsConstructor
public class TileOwnershipSnapshot {
    private int tilePosition;
    private UUID ownerPlayerId;

    public TileOwnershipSnapshot(int tilePosition, UUID ownerPlayerId) {
        this.tilePosition = tilePosition;
        this.ownerPlayerId = ownerPlayerId;
    }
}
