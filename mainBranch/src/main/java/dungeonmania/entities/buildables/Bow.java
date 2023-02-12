package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class Bow extends Buildable {

    private int durability;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
    }

    @Override
    public void use(Player player) {
        durability--;
        if (durability <= 0) {
            player.remove(this);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
                0,
                0,
                0,
                2,
                1));
    }

    @Override
    public int getDurability() {
        return durability;
    }
}
