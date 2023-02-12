package dungeonmania.direction;

public class Back extends Direction {
    public Back(int position) {
        super(position);
    }

    public int findNextPosition() {
        setPosition(getNextPositionElement() - 1);
        if (getNextPositionElement() == -1) {
            setPosition(7);
        }
        return getNextPositionElement();
    }

    public Direction switchDirection() {
        return new Forward(getNextPositionElement());
    }

}
