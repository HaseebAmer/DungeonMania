package dungeonmania.entities;

import dungeonmania.entities.MovementState.DefaultState;
import dungeonmania.entities.MovementState.MovementState;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.UUID;

public abstract class Entity {
    public static final int FLOOR_LAYER = 0;
    public static final int ITEM_LAYER = 1;
    public static final int DOOR_LAYER = 2;
    public static final int CHARACTER_LAYER = 3;

    private Position position;
    private Position previousPosition;
    private Position previousDistinctPosition;
    private Direction facing;
    private String entityId;
    private MovementState movementState;

    public Entity(Position position) {
        this.position = position;
        this.previousPosition = position;
        this.previousDistinctPosition = null;
        this.entityId = UUID.randomUUID().toString();
        this.facing = null;
        this.movementState = new DefaultState();
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return false;
    }

    // use setPosition
    @Deprecated(forRemoval = true)
    public void translate(Direction direction) {
        previousPosition = this.position;
        this.position = Position.translateBy(this.position, direction);
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    // use setPosition
    @Deprecated(forRemoval = true)
    public void translate(Position offset) {
        this.position = Position.translateBy(this.position, offset);
    }

    public void onOverlap(GameMap map, Entity entity) {
    };

    public void onMovedAway(GameMap map, Entity entity) {
    };

    public void onDestroy(GameMap gameMap) {
    };

    public Position getPosition() {
        return position;
    }

    public List<Position> getAdjacentPositions() {
        return position.getAdjacentPositions();
    }

    public List<Position> getCardinallyAdjacentPositions() {
        return position.getCardinallyAdjacentPositions();
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    public Position getPreviousDistinctPosition() {
        return previousDistinctPosition;
    }

    public String getId() {
        return entityId;
    }

    public void setPosition(Position position) {
        previousPosition = this.position;
        this.position = position;
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public Direction getFacing() {
        return this.facing;
    }

    public void setPrevPosition() {
        previousPosition = getPosition();
    }

    public boolean comparePositions(Position a, Position b) {
        return a.getX() == b.getX() && a.getY() == b.getY();
    }

    public void setMovementState(MovementState ms) {
        this.movementState = ms;
    }

    public MovementState getMovementState() {
        return movementState;
    }
}
