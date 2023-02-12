package dungeonmania.entities.collectables.CollectableStrategies;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface CollectableStrategy {
    public void pick(GameMap map, Entity entity);


    public boolean onOverlap(Entity entity);
}
