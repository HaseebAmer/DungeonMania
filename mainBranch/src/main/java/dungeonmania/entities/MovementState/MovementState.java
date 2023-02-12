package dungeonmania.entities.MovementState;



public interface MovementState {
    public void onTick();

    public boolean canMove();
}
