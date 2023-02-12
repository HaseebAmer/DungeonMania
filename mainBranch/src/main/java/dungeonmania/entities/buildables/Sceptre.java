package dungeonmania.entities.buildables;

import java.util.List;


import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Mercenary;


public class Sceptre extends Buildable {
    private int mindControlDuration;
    private Mercenary mindControlled;

    public Sceptre(int mindControlDuration) {
        super(null);
        this.mindControlDuration = mindControlDuration;
        this.mindControlled = null;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
                0,
                0,
                0,
                1,
                1));
    }

    @Override
    public void use(Player player) {
        return;
    }

    @Override
    public int getDurability() {
        return mindControlDuration;
    }

    public boolean getIsInControl() {
        return mindControlled != null;
    }

    public void mindControl(Mercenary mercenary) {
        this.mindControlled = mercenary;
    }

    public Mercenary getMindControlled() {
        return mindControlled;
    }

    public void reduceMindControlDuration() {
        this.mindControlDuration--;
    }

    public static void tick(Player player) {
        if (player == null) {
            return;
        }
        List<Sceptre> sceptres = player.getInventory().getEntities(Sceptre.class);
        if (sceptres.size() == 0)
            return;

        Sceptre sceptre = sceptres.get(0);
        if (!sceptre.getIsInControl())
            return;

        sceptre.reduceMindControlDuration();

        if (sceptre.getDurability() <= 0) {
            player.remove(sceptre);
            sceptre.getMindControlled().setMindControlled(false);
            sceptre.mindControl(null);
        }
    }

}
