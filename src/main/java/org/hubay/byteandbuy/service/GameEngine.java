package service;

import model.game.Game;
import model.player.Player;
import model.tiles.PropertyTile;
import model.tiles.Tile;

import java.util.List;
import java.util.Random;

// Herna logika.
public class GameEngine {
    // metoda simuluje hod kockou.
    public int rollDice(){
        return new Random().nextInt(6)+1;
    }

    // Ziska hraca ktory je aktualne na tahu.
    public Player getCurrentPlayer(Game game){
        return game.getPlayers().get(game.getCurrentPlayerIndex());
    }

    // Posunie hraca podla hodu kockou na nove hracie policko.
    public void movePlayer(Game game, int steps){
        Player player = getCurrentPlayer(game);

        int from = player.getPosition();
        int boardSize = game.getBoard().getTiles().size();
        int to = (from + steps) % boardSize;

        player.setPosition(to);
    }

    // skontroluje ci hrac ma na ucte peniaze,
    // ak nie vyradi ho z hry a jeho kupene policka nastavi na znovu kupitelne.
    private void checkBankruptcy(Game game, Player player) {
        if (player.getMoney() <= 0) {
            System.out.println(player.getName() + " skrachoval/-la");
            player.setInGame(false);

            for (Tile tile : game.getBoard().getTiles()) {
                tile.onPlayerBankrupt(player);
            }
        }
    }

    // ukonci tah hraca a posunie tah na dalsieho hraca.
    public void endTurn(Game game){
        int activePlayers = 0;

        for (Player p : game.getPlayers()) {
            if (p.isInGame()) {
                activePlayers++;
            }
        }

        if (activePlayers <= 1) {
            game.setFinished(true);
            return;
        }

        int nextPlayer = game.getCurrentPlayerIndex();

        do {
            nextPlayer = (nextPlayer + 1) % game.getPlayers().size();
        } while (!game.getPlayers().get(nextPlayer).isInGame());

        game.setCurrentPlayerIndex(nextPlayer);
        System.out.println();
    }

    public void playTurn(Game game) {
        Player player = getCurrentPlayer(game);
        int from = player.getPosition();

        System.out.println(player.getName() + " je na " +
                game.getBoard().getTiles().get(player.getPosition()).getName());
        System.out.println(player.getName() + " ma na ucte PRED: " + player.getMoney());

        int dice = rollDice();
        System.out.println("Hod kockou: " + dice);

        movePlayer(game, dice);
        handleStartPassing(game, player, from, dice);

        resolveTileEffect(game, player);

        if (dice != 6) {
            endTurn(game);
        } else {
            System.out.println(player.getName() + " hodil 6 → hrá znova");
        }
    }

    // Bonus ak hrac prejde cez start.
    private void handleStartPassing(Game game, Player player, int from, int steps) {
        int boardSize = game.getBoard().getTiles().size();

        if (from + steps > boardSize) {
            Tile startTile = game.getBoard().getStartTile();
            startTile.interact(game, player);
        }
    }

    public void resolveTileEffect(Game game, Player player) {
        int previousPosition;

        do {
            previousPosition = player.getPosition();

            List<Tile> tiles = game.getBoard().getTiles();
            if(tiles.get(player.getPosition()) instanceof PropertyTile property && property.getOwner() != null) {
                System.out.println(player.getName() + " sa posunul na " + tiles.get(player.getPosition()).getName() +
                        " vlastni ho " + property.getOwner().getName());
            } else {
                System.out.println(player.getName() + " sa posunul na " + tiles.get(player.getPosition()).getName());
            }

            Tile tile = game.getBoard().getTiles().get(player.getPosition());
            tile.interact(game, player);

            checkBankruptcy(game, player);
            System.out.println(player.getName() + " ma na ucte PO: " + player.getMoney());

        } while (player.getPosition() != previousPosition);
    }
}
