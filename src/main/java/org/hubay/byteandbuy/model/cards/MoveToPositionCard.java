package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Karticka obsahujuca posun hraca na konkretne policko.
public class MoveToPositionCard implements Card {
    // pozicia kam sa ma hrac posunut.
    private final int position;
    // Popis na karte.
    private final String description;

    public MoveToPositionCard(int position, String description) {
        this.position = position;
        this.description = description;
    }

    // Vykona presun hraca na danu poziciu na hracej doske.
    @Override
    public CardResult apply(Game game, Player player) {
        boolean passedStart = player.moveTo(position);

        if (passedStart) {
            System.out.println("ziskal bonus za START posunutim kartou na START");
            player.receive(game.getGameConfig().getStartBonus());
        }

        return CardResult.simple("Posun na " + description);
    }

    // Popis na karte.
    @Override
    public String getDescription() {
        return description;
    }
}
