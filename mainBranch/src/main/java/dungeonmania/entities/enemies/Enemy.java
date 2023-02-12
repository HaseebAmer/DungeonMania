package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Overlap;
import dungeonmania.entities.SwampTile;
import dungeonmania.entities.ItemStrategies.EnemyStrategy;
import dungeonmania.entities.MovementState.SwampState;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Overlap implements Battleable {
    private BattleStatistics battleStatistics;

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER), new EnemyStrategy());
        battleStatistics = new BattleStatistics(
                health,
                attack,
                0,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public double getHealth() {
        return battleStatistics.getHealth();
    }

    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    protected void applySpawnedOnSwampDebuff(Game game) {
        if (game.getTick() != 0) {
            return;
        }
        GameMap map = game.getMap();
        List<Entity> entities = map.getEntities(getPosition());
        for (Entity e : entities) {
            if (e instanceof SwampTile) {
                setMovementState(new SwampState(((SwampTile) e).getMovementFactor() - 1, this));
            }
        }
    }

    public abstract void move(Game game);
}
