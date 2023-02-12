package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.Spawner;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends Spawner implements Interactable {
    public static final int DEFAULT_SPAWN_INTERVAL = 0;

    public ZombieToastSpawner(Position position, int spawnInterval) {
        super(position);
    }

    public void spawn(Game game) {
        game.spawnZombie(this);
    }

    @Override
    public void interact(Player player, Game game) {
        player.useWeapon();
        game.destroyEntity(this);
    }

    @Override
    public boolean isInteractable(Player player) {
        return Position.isAdjacent(player.getPosition(), getPosition()) && player.hasWeapon();
    }

}
