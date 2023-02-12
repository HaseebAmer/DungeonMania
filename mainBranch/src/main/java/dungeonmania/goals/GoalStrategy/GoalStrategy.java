package dungeonmania.goals.GoalStrategy;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public interface GoalStrategy {
    public boolean isAchieved(Game game, int target, Goal goal1, Goal goal2);

    public String toString(Game game, int target, Goal goal1, Goal goal2);

}
