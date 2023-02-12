package dungeonmania;

import java.io.IOException;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.goals.Goal;
import dungeonmania.goals.GoalFactory;
import dungeonmania.map.GameMap;
import dungeonmania.map.GraphNode;
import dungeonmania.map.GraphNodeFactory;
import dungeonmania.util.FileLoader;

/**
 * GameBuilder -- A builder to build up the whole game
 *
 * @author Webster Zhang
 * @author Tina Ji
 */
public class GameBuilder {
    private String configName;
    private String dungeonName;

    private JSONObject config;
    private JSONObject dungeon;

    public GameBuilder setConfigName(String configName) {
        this.configName = configName;
        return this;
    }

    public GameBuilder setDungeonName(String dungeonName) {
        this.dungeonName = dungeonName;
        return this;
    }

    public Game buildGame() {
        loadConfig();
        loadDungeon();
        if (dungeon == null && config == null) {
            return null; // something went wrong
        }

        Game game = new Game(dungeonName);
        EntityFactory factory = new EntityFactory(config);
        game.setEntityFactory(factory);
        game.setConfigName(configName);
        buildMap(game);
        buildGoals(game);
        game.init();

        return game;
    }

    public Game buildGame(String dungeonName, String configName) {
        loadConfig(configName);
        loadDungeon(dungeonName);
        if (dungeon == null && config == null) {
            return null; // something went wrong
        }

        Game game = new Game(dungeonName);
        EntityFactory factory = new EntityFactory(config);

        game.setEntityFactory(factory);
        game.setConfigName(configName);

        buildMap(game);
        buildGoals(game);
        game.init();
        dungeon.getJSONArray("inventory").forEach(e -> {
            JSONObject jsonEntity = (JSONObject) e;
            Entity entity = factory.constructItem(jsonEntity);
            game.getInventory().add((InventoryItem) entity);
        });

        return game;
    }

    private void loadConfig() {
        String configFile = String.format("/configs/%s.json", configName);
        try {
            config = new JSONObject(FileLoader.loadResourceFile(configFile));
        } catch (IOException e) {
            e.printStackTrace();
            config = null;
        }
    }

    private void loadConfig(String name) {
        String configFile = String.format("/configs/%s.json", name);
        try {
            config = new JSONObject(FileLoader.loadResourceFile(configFile));
        } catch (IOException e) {
            e.printStackTrace();
            config = null;
        }
    }

    private void loadDungeon() {
        String dungeonFile = String.format("/dungeons/%s.json", dungeonName);
        try {
            dungeon = new JSONObject(FileLoader.loadResourceFile(dungeonFile));
        } catch (IOException e) {
            dungeon = null;
        }
    }

    private void loadDungeon(String name) {
        String dungeonFile = String.format("/saves/" + name + ".json");
        try {
            dungeon = new JSONObject(FileLoader.loadResourceFile(dungeonFile));
        } catch (IOException e) {
            dungeon = null;
        }
    }

    private void buildMap(Game game) {
        GameMap map = new GameMap();
        map.setGame(game);

        dungeon.getJSONArray("entities").forEach(e -> {
            JSONObject jsonEntity = (JSONObject) e;
            GraphNode newNode = GraphNodeFactory.createEntity(jsonEntity, game.getEntityFactory());
            Entity entity = newNode.getEntities().get(0);

            if (newNode != null)
                map.addNode(newNode);

            if (entity instanceof Player)
                map.setPlayer((Player) entity);
        });
        game.setMap(map);
    }

    public void buildGoals(Game game) {
        if (!dungeon.isNull("goal-condition")) {
            Goal goal = GoalFactory.createGoal(dungeon.getJSONObject("goal-condition"), config);
            game.setGoals(goal);
        }
    }
}
