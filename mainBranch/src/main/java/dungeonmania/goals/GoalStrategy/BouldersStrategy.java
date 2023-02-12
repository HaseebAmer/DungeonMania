package dungeonmania.goals.GoalStrategy;

import dungeonmania.Game;
import dungeonmania.entities.Switch;
import dungeonmania.goals.Goal;

public class BouldersStrategy implements GoalStrategy {

    @Override
    public boolean isAchieved(Game game, int target, Goal goal1, Goal goal2) {
        return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    @Override
    public String toString(Game game, int target, Goal goal1, Goal goal2) {
        return ":boulders";
    }

}
