package dungeonmania.entities.ItemStrategies;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;

public class EnemyStrategy implements ItemStrategies {

    public EnemyStrategy() {
    }

    @Override
    public void onOverlap(GameMap map, Entity entity1, Entity entity2) {
        if (entity2 instanceof Player && entity1 instanceof Enemy) {
            if (entity1 instanceof Mercenary) {
                if (((Mercenary) entity1).isAllied())
                    return;
            }

            map.doBattle((Player) entity2, (Enemy) entity1);
        }
    }

}
