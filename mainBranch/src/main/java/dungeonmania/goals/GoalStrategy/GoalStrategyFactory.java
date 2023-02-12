package dungeonmania.goals.GoalStrategy;

public class GoalStrategyFactory {
    public static GoalStrategy createStrategy(String type) {
        switch (type) {
            case "AND":
                return new AndStrategy();
            case "OR":
                return new OrStrategy();
            case "exit":
                return new ExitStrategy();
            case "boulders":
                return new BouldersStrategy();
            case "treasure":
                return new TreasureStrategy();
            case "enemies":
                return new EnemyStrategy();
            default:
                return null;
        }

    }
}
