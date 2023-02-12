package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.entities.ItemStrategies.DoorStrategy;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.util.Position;

public class Door extends Overlap {
    private boolean open = false;
    private int number;

    public Door(Position position, int number) {
        super(position.asLayer(Entity.DOOR_LAYER), new DoorStrategy());
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (open || entity instanceof Spider) {
            return true;
        }
        return (entity instanceof Player && (hasKey((Player) entity)));
    }

    public void keymethod(Player player, Key key, Inventory inventory) {
        if (hasKey(player)) {
            inventory.remove(key);
            open();
        }
    }

    public boolean stoneMethod(SunStone stone) {
        if (stone != null) {
            open();
            return true;
        }
        return false;
    }

    private boolean hasKey(Player player) {
        return player.hasKey(number);
    }

    public int getNumber() {
        return number;
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

}
