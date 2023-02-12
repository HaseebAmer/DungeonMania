package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.CollectableStrategies.CollectableStrategy;
import dungeonmania.entities.collectables.CollectableStrategies.DefaultStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Collectable extends Entity {
    private CollectableStrategy strategy;

    public Collectable(Position position) {
        super(position);
        strategy = new DefaultStrategy();
    }

    public Collectable(Position position, CollectableStrategy strategy) {
        super(position);
        this.strategy = strategy;
    }

    protected void setStrategy(CollectableStrategy strategy) {
        this.strategy = strategy;
    }

    public void pick(GameMap map, Entity entity) {

        strategy.pick(map, entity);
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        return;
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }

    // public void onOverlapped(GameMap map, Entity entity) {
    // if (!strategy.onOverlap(this)) return;
    // if (entity instanceof Player) {
    // if (!((Player) entity).pickUp(this)) return;
    // strategy.pick(map, this);
    // }
    // }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!strategy.onOverlap(this))
            return;
        if (entity instanceof Player) {
            if (!((Player) entity).pickUp(this))
                return;
            strategy.pick(map, this);
        }
    }
}
