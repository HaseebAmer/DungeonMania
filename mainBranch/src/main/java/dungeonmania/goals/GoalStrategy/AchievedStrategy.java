package dungeonmania.goals.GoalStrategy;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public class AchievedStrategy implements GoalStrategy {

    @Override
    public boolean isAchieved(Game game, int target, Goal goal1, Goal goal2) {
        return false;
    }

    @Override
    public String toString(Game game, int target, Goal goal1, Goal goal2) {
        return "";
    }

}
