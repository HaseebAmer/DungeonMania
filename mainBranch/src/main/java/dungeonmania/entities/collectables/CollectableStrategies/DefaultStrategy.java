package dungeonmania.entities.collectables.CollectableStrategies;

import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;

public class DefaultStrategy implements CollectableStrategy {

    public DefaultStrategy() {
    }

    @Override
    public void pick(GameMap map, Entity entity) {
        if (entity instanceof InventoryItem) {
            map.destroyEntity(entity);
        }
    }

    @Override
    public boolean onOverlap(Entity entity) {
        return true;
    }
}
