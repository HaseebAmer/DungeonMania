package dungeonmania.entities.ItemStrategies;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;

public class PlayerStrategy implements ItemStrategies {

    public PlayerStrategy() {
    }

    @Override
    public void onOverlap(GameMap map, Entity entity1, Entity entity2) {
        if (entity2 instanceof Enemy && entity1 instanceof Player) {
            if (entity2 instanceof Mercenary) {
                if (((Mercenary) entity2).isAllied())
                    return;
            }
            map.doBattle((Player) entity1, (Enemy) entity2);
        }
    }
}
