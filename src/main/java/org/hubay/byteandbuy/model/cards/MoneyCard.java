package model.cards;

import model.game.Game;
import model.player.Player;

public class MoneyCard implements Card {
    private final int money;
    private final String description;

    public MoneyCard(int money, String description) {
        this.money = money;
        this.description = description;
    }

    @Override
    public void apply(Game game, Player player) {
        player.setMoney(player.getMoney() + money);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
