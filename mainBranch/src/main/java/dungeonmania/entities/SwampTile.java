package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity {
    private int movementFactor;
    private int defaultMovementFactor;

    public SwampTile(Position position, int movementFactor) {
        super(position);
        this.movementFactor = 2 * movementFactor - 1;
        this.defaultMovementFactor = movementFactor;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public int getMovementFactor() {
        return this.movementFactor;
    }

    public int getDefaultMovementFactor() {
        return defaultMovementFactor;
    }

}
