package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.goals.GoalStrategy.GoalStrategy;
import dungeonmania.goals.GoalStrategy.GoalStrategyFactory;

public class Goal {
    private GoalStrategy strategy;
    private int target;
    private Goal goal1;
    private Goal goal2;

    public Goal(String type) {
        this.strategy = GoalStrategyFactory.createStrategy(type);
    }

    public Goal(String type, int target) {
        this.target = target;
        this.strategy = GoalStrategyFactory.createStrategy(type);

    }

    public Goal(String type, Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
        this.strategy = GoalStrategyFactory.createStrategy(type);

    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return strategy.isAchieved(game, target, goal1, goal2);
    }

    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return strategy.toString(game, target, goal1, goal2);
    }

}
