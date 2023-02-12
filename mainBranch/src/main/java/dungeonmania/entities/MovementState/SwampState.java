package dungeonmania.entities.MovementState;

import dungeonmania.entities.Entity;

public class SwampState implements MovementState {
    private Entity entity;
    private int movementFactor;

    public SwampState(int movementFactor, Entity entity) {
        this.movementFactor = movementFactor;
        this.entity = entity;
    }

    @Override
    public void onTick() {
        this.movementFactor--;
        if (movementFactor <= 0) {
            entity.setMovementState(new DefaultState());
        }
    }

    @Override
    public boolean canMove() {
        return false;
    }

}
