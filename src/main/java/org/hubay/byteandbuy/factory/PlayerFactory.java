package org.hubay.byteandbuy.factory;

import org.hubay.byteandbuy.model.player.Player;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Vytvara novych hracov alebo obnovuje ich zo snapshotu.
 */
@Component
public class PlayerFactory {
    /**
     * Vytvori noveho hraca s inokatnym ID.
     */
    public Player createNew(String name, int position, int money, boolean inGame) {
        return new Player(UUID.randomUUID(), name, position, money, inGame);
    }

    /**
     * Obnovu hraca z DB (zo snapshotu).
     * Hracovy prida existujuce ID a vsetky dalsie ulozene atributy.
     */
    public Player restore(UUID id, String name, int position, int money, boolean inGame, boolean inJail) {
        return new Player(id, name, position, money, inGame, inJail);
    }
}
