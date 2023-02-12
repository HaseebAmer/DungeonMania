package dungeonmania.direction;

public abstract class Direction {
    private int position;

    public Direction(int position) {
        this.position = position;
    }

    public int getNextPositionElement() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public abstract int findNextPosition();

    public abstract Direction switchDirection();

}
