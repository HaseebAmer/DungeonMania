package dungeonmania.goals.GoalStrategy;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public class AndStrategy implements GoalStrategy {

    @Override
    public boolean isAchieved(Game game, int target, Goal goal1, Goal goal2) {
        return goal1.achieved(game) && goal2.achieved(game);
    }

    @Override
    public String toString(Game game, int target, Goal goal1, Goal goal2) {
        return "(" + goal1.toString(game) + " AND " + goal2.toString(game) + ")";
    }

}
