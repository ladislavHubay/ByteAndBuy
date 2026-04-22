package org.hubay.byteandbuy.model.tiles;

/**
 * Reprezentuje vysledok interakcie hraca.
 * Urcuje ci hra pokracuje automaticky alebo sa caka na rozhodnutie hraca.
 */
public enum TileResult {
    NOTHING,
    DRAW_CARD,
    WAIT_FOR_PURCHASE,
    PAY_RENT
}
