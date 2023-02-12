package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {

    public static final double DEFAULT_FAIL_RATE = 0;
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;


    private double assassinBribeFailRate = Assassin.DEFAULT_FAIL_RATE;

    public Assassin(Position position, double health, double attack,
            int bribeAmount, int bribeRadius, double assassinBribeFailRate) {
        super(position, health, attack, bribeAmount, bribeRadius);
        this.assassinBribeFailRate = assassinBribeFailRate;
    }

    @Override
    public void interact(Player player, Game game) {
        Random rand = new Random();
        int randNum = rand.nextInt(100);
        if (!player.isSceptreReady()) {
            if (randNum >= 100 * assassinBribeFailRate) {
                bribe(player);
                setMindControlled(true);
            } else {
                bribe(player);
            }
        } else {
            setMindControlled(true);
            bribe(player);
        }

    }
}
