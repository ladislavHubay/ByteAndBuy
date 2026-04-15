package model.cards;

import model.game.Game;
import model.player.Player;

public class MoveStepsCard implements Card{
    private final int steps;
    private final String description;

    public MoveStepsCard(int steps, String description) {
        this.steps = steps;
        this.description = description;
    }

    @Override
    public void apply(Game game, Player player) {
        player.setPosition(player.getPosition() + steps);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
