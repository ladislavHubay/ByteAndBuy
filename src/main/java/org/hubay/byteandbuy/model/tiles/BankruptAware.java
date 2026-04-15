package org.hubay.byteandbuy.model.tiles;

import org.hubay.byteandbuy.model.player.Player;

public interface BankruptAware {
    void onPlayerBankrupt(Player player);
}
