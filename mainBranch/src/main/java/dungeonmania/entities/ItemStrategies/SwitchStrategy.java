package dungeonmania.entities.ItemStrategies;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;

import dungeonmania.map.GameMap;

public class SwitchStrategy implements ItemStrategies {

    public SwitchStrategy() {
    }

    @Override
    public void onOverlap(GameMap map, Entity entity1, Entity entity2) {
        if (entity2 instanceof Boulder && entity1 instanceof Switch) {
            Switch switch1 = (Switch) entity1;
            switch1.setActivated(true);
            switch1.notify(map);
        }
    }
}
