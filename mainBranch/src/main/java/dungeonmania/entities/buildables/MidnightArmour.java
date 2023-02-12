package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class MidnightArmour extends Buildable {
    private double defence;
    private double attack;

    public MidnightArmour(double attack, double defence) {
        super(null);
        this.defence = defence;
        this.attack = attack;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
                0,
                attack,
                defence,
                1,
                1));

    }

    @Override
    public void use(Player player) {
    }

    @Override
    public int getDurability() {
        return 1;
    }

}
