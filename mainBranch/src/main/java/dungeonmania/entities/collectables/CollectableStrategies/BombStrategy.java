package dungeonmania.entities.collectables.CollectableStrategies;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;

public class BombStrategy implements CollectableStrategy {

    public BombStrategy() {
    }

    @Override
    public void pick(GameMap map, Entity entity) {
        Bomb bomb = (Bomb) entity;
        bomb.unsubscribeAll();
        map.destroyEntity(entity);
    }

    @Override
    public boolean onOverlap(Entity entity) {
        Bomb bomb = (Bomb) entity;
        return bomb.onOverlapped();
    }

}
