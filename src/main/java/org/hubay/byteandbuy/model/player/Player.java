package model.player;

// hrac
public class Player {
    // Nick hraca.
    private final String name;
    // aktualna pozicia na hracej doske (Board - index v List<Tile> tiles;).
    private int position;
    // aktualna suma penazi ktore hrac ma.
    private int money;
    // ci je hrac v hre alebo skoncil.
    private boolean inGame;             // pre budúcnosť (bankrot)

    public Player(String name, int position, int money, boolean inGame) {
        this.name = name;
        this.position = position;
        this.money = money;
        this.inGame = inGame;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public int getMoney() {
        return money;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
