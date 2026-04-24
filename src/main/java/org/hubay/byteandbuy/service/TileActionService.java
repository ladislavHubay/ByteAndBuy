package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.model.cards.Card;
import org.hubay.byteandbuy.model.game.Game;
import org.hubay.byteandbuy.model.player.Player;
import org.hubay.byteandbuy.model.tiles.Tile;
import org.hubay.byteandbuy.model.tiles.TileActionType;
import org.springframework.stereotype.Service;

/**
 * Vyhodnocuje efekty policok.
 */
@Service
public class TileActionService {
    private final PlayerStateService playerStateService;

    public TileActionService(PlayerStateService playerStateService) {
        this.playerStateService = playerStateService;
    }

    /**
     * Vyhodnocuje efekt policka na ktorom hrac stoji.
     * Podporuje retazenie efektov (policko -> tahanie karty -> posun na dalsie policko...)
     */
    public void resolveTileEffects(Game game, Player player){
        Tile tile = game.getCurrentTile(player);
        TileActionType result = tile.interact(game, player);

        switch (result.getType()){
            case WAIT_FOR_PURCHASE -> {
                game.getEventCollector().add(player.getName() + " moze kupit " + tile.getName());
                game.waitForBuy();
            }
            case PAY_RENT -> {
                game.getEventCollector().add(player.getName() + " plati najom " + result.getRent() + " pre " + result.getOwner().getName());
                result.getOwner().receive(result.getRent());

                player.pay(result.getRent());
                game.getEventCollector().add(player.getName() + " zostalo na ucte " + player.getMoney());
                playerStateService.checkBankruptcy(game, player);
            }

            case DRAW_CARD -> {
                game.waitForCard();
                game.getEventCollector().add(player.getName() + " potiahni si kartu");
            }

            case NOTHING -> {}
        }
    }

    public void drawCard(Game game, Player player){
        Tile tile = game.getCurrentTile(player);

        TileActionType result = tile.interact(game, player);

        Card card = result.getDeck().draw();
        game.getEventCollector().add(player.getName() + " potiahol kartu " + card.getDescription());
        card.apply(game, player);

        playerStateService.checkBankruptcy(game, player);
        game.resumePlaying();
    }
}
