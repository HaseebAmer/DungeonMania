package dungeonmania.goals.GoalStrategy;

import dungeonmania.Game;
import dungeonmania.Spawner;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.goals.Goal;

public class EnemyStrategy implements GoalStrategy {

    @Override
    public boolean isAchieved(Game game, int target, Goal goal1, Goal goal2) {
        return game.getInitialEnemyCount() - game.getMap().getEntities(Enemy.class).size() >= target
                && game.getMap().getEntities(Spawner.class).size() == 0;
    }

    @Override
    public String toString(Game game, int target, Goal goal1, Goal goal2) {
        return ":enemies";
    }

}
