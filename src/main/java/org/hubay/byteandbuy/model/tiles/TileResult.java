package org.hubay.byteandbuy.model.tiles;

/**
 * Reprezentuje vysledok interakcie hraca.
 * Urcuje ci hra pokracuje automaticky alebo sa caka na rozhodnutie hraca.
 */
public enum TileResult {
    CONTINUE,
    WAIT_FOR_DECISION
}
