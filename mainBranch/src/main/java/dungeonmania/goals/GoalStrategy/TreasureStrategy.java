package dungeonmania.goals.GoalStrategy;

import dungeonmania.Game;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.goals.Goal;

public class TreasureStrategy implements GoalStrategy {

    @Override
    public boolean isAchieved(Game game, int target, Goal goal1, Goal goal2) {
        return game.getInitialTreasureCount() - game.getMap().getEntities(Treasure.class).size() >= target; // code
                                                                                                            // chain
    }

    @Override
    public String toString(Game game, int target, Goal goal1, Goal goal2) {
        return ":treasure";
    }

}
