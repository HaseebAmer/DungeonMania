package dungeonmania.entities.ItemStrategies;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface ItemStrategies {
    public void onOverlap(GameMap map, Entity entity, Entity entity2);
}
