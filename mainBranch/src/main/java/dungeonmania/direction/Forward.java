package dungeonmania.direction;

public class Forward extends Direction {
    public Forward(int position) {
        super(position);
    }

    public int findNextPosition() {
        setPosition(getNextPositionElement() + 1);
        if ((getNextPositionElement() == 8)) {
            setPosition(0);
        }
        return getNextPositionElement();
    }

    public Direction switchDirection() {
        return new Back(getNextPositionElement());
    }

}
