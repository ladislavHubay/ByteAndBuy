package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.player.Player;

/**
 * Rozhranie pre vsetky typy policok ktore sa daju kupit.
 * Pouziva sa na odstranenie vlastnictva ak hrac je z akychkolvek dovodov vyradeny z hry.
 */
public interface BankruptAware {
    void onPlayerBankrupt(Player player);
}
