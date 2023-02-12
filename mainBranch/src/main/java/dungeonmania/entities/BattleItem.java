package dungeonmania.entities;

import dungeonmania.battles.BattleStatistics;

/**
 * Item has buff in battles
 */
public interface BattleItem {
    public BattleStatistics applyBuff(BattleStatistics origin);

    public void use(Player player);

    public int getDurability();
}
