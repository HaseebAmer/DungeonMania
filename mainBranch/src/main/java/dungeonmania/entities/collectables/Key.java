package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.CollectableStrategies.KeyStrategy;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Key extends Collectable implements InventoryItem {
    private int number;

    public Key(Position position, int number) {
        super(position, new KeyStrategy());
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public int getNumber() {
        return number;
    }

}
