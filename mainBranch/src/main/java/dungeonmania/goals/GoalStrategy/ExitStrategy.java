package dungeonmania.goals.GoalStrategy;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.goals.Goal;
import dungeonmania.util.Position;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;

public class ExitStrategy implements GoalStrategy {

    @Override
    public boolean isAchieved(Game game, int target, Goal goal1, Goal goal2) {
        Player character = game.getPlayer();
        Position pos = character.getPosition();
        List<Exit> es = game.getMap().getEntities(Exit.class);
        if (es == null || es.size() == 0)
            return false;
        return es
                .stream()
                .map(Entity::getPosition)
                .anyMatch(pos::equals);
    }

    @Override
    public String toString(Game game, int target, Goal goal1, Goal goal2) {
        return ":exit";
    }

}
