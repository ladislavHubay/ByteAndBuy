package model.cards;

import model.game.Game;
import model.player.Player;

public class MoveToPositionCard implements Card {
    private final int position;
    private final String description;

    public MoveToPositionCard(int position, String description) {
        this.position = position;
        this.description = description;
    }

    @Override
    public void apply(Game game, Player player) {
        player.setPosition(game.getBoard().getTiles().get(position).getPosition());
    }

    @Override
    public String getDescription() {
        return description;
    }
}
