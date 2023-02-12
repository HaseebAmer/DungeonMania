package dungeonmania.entities.ItemStrategies;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;

public class BoulderStrategy implements ItemStrategies {

    public BoulderStrategy() {
    }

    @Override
    public void onOverlap(GameMap map, Entity entity1, Entity entity2) {
        if (entity1 instanceof Boulder && entity2 instanceof Player) {
            Boulder boulder = (Boulder) entity1;
            map.moveTo(boulder, entity2.getFacing());
        }
    }

}
