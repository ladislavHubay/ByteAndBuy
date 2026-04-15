package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;

// Karticka obsahujuca nahodnu akciu (posun hraca o dany pocet krokov).
public class MoveStepsCard implements Card{
    // Pocet krokov o kolko sa hrac posunie.
    private final int steps;
    // Popis na karte.
    private final String description;

    public MoveStepsCard(int steps, String description) {
        this.steps = steps;
        this.description = description;
    }

    // Metoda vykona posun hraca.
    @Override
    public CardResult apply(Game game, Player player) {
        boolean passedStart = player.move(steps, game.getBoardSize());

        if (passedStart) {
            System.out.println("ziskal bonus za START posunutim kartou o ... krokov");
            player.receive(game.getGameConfig().getStartBonus());
        }

        return CardResult.simple("posun o " + steps + " krokov");
    }

    // Vrati popis na karte.
    @Override
    public String getDescription() {
        return description;
    }
}
