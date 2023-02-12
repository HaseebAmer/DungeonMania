package dungeonmania.entities.ItemStrategies;

import dungeonmania.entities.Door;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.map.GameMap;

public class DoorStrategy implements ItemStrategies {

    public DoorStrategy() {
    }

    @Override
    public void onOverlap(GameMap map, Entity entity1, Entity entity2) {

        if (entity1 instanceof Door && entity2 instanceof Player) {
            Player player = (Player) entity2;
            Door door = (Door) entity1;
            Inventory inventory = player.getInventory();
            SunStone stone = inventory.getFirst(SunStone.class);
            if (!door.stoneMethod(stone)) {
                Key key = inventory.getFirst(Key.class);
                door.keymethod(player, key, inventory);
            }
        }
    }

}
