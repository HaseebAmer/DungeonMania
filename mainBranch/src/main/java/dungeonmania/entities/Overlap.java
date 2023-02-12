package dungeonmania.entities;

import dungeonmania.entities.ItemStrategies.ItemStrategies;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Overlap extends Entity {
    private ItemStrategies strategy;

    public Overlap(Position position) {
        super(position);
    }

    public Overlap(Position position, ItemStrategies strategy) {
        super(position);
        this.strategy = strategy;
    }

    protected void setStrategy(ItemStrategies strategy) {
        this.strategy = strategy;
    }

    public void onOverlap(GameMap map, Entity entity) {
        strategy.onOverlap(map, this, entity);
    }
}
