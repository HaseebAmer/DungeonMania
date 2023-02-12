package dungeonmania.entities.MovementState;



public class DefaultState implements MovementState {
    public DefaultState() {
    }

    @Override
    public void onTick() {
        return;
    }

    @Override
    public boolean canMove() {
        return true;
    }

}
