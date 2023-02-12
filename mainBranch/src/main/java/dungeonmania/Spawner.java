package dungeonmania;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public abstract class Spawner extends Entity {

    public Spawner(Position position) {
        super(position);
    }

    public abstract void spawn(Game game);

}
