package dungeonmania;

import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dungeonmania.entities.Door;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Portal;
import dungeonmania.entities.SwampTile;
import dungeonmania.entities.collectables.Key;

import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.map.GameMap;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class DungeonManiaController {
    private Game game = null;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        if (!dungeons().contains(dungeonName)) {
            throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
        }

        if (!configs().contains(configName)) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        try {
            GameBuilder builder = new GameBuilder();
            game = builder.setConfigName(configName).setDungeonName(dungeonName).buildGame();
            return ResponseBuilder.getDungeonResponse(game);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return null;
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(game.tick(itemUsedId));
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        return ResponseBuilder.getDungeonResponse(game.tick(movementDirection));
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        List<String> validBuildables = List.of("bow", "shield", "midnight_armour", "sceptre");
        if (!validBuildables.contains(buildable)) {
            throw new IllegalArgumentException("Only bow, shield, midnight_armour and sceptre can be built");
        }

        return ResponseBuilder.getDungeonResponse(game.build(buildable));
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(game.interact(entityId));
    }

    /**
     * /game/save
     *
     * @throws IOException
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException, IOException {
        JSONArray dungeon = new JSONArray();
        GameMap map = game.getMap();
        JSONObject outer = new JSONObject();
        List<EntityResponse> entities = ResponseBuilder.getDungeonResponse(game).getEntities();
        for (EntityResponse e : entities) {
            JSONObject entity = new JSONObject();
            Entity obj = map.getEntity(e.getId());
            String type = e.getType();

            if (type.contains("portal_")) {
                type = "portal";
            }

            entity.put("type", type);
            entity.put("x", e.getPosition().getX());
            entity.put("y", e.getPosition().getY());
            if (e.getType().equals("door")) {
                entity.put("key", ((Door) obj).getNumber());
            } else if (type.equals("key")) {
                entity.put("key", ((Key) obj).getNumber());
            } else if (type.equals("swamp_tile")) {
                entity.put("movement_factor", ((SwampTile) obj).getDefaultMovementFactor());
            } else if (type.equals("portal")) {
                entity.put("colour", ((Portal) obj).getColor());
            }
            dungeon.put(entity);
        }
        outer.put("entities", dungeon);
        String dungeonFile = String.format("/dungeons/%s.json", game.getName());
        if (!FileLoader.listFileNamesInResourceDirectory("dungeons").contains(game.getName())) {
            dungeonFile = String.format("/saves/%s.json", game.getName());
        }
        String thisDungeon = FileLoader.loadResourceFile(dungeonFile);
        JSONObject dungeonJSON = new JSONObject(thisDungeon);
        JSONObject goal = dungeonJSON.getJSONObject("goal-condition");
        outer.put("goal-condition", goal);
        outer.put("config", game.getConfigName());
        outer.put("dungeon", game.getName());
        List<InventoryItem> inventory = game.getInventory().getEntities(InventoryItem.class);
        JSONArray items = new JSONArray();
        for (InventoryItem item : inventory) {
            JSONObject i = new JSONObject();
            String itemType = item.getClass().getSimpleName();
            if (itemType.equals("Key")) {
                i.put("key", ((Key) item).getNumber());
            }
            i.put("item_type", item.getClass().getSimpleName());
            items.put(i);
        }
        outer.put("inventory", items);

        try {
            String path = FileLoader.getPathForNewFile("/saves", name);
            String fullName = path + ".json";
            FileWriter file = new FileWriter(fullName, false);
            file.write(outer.toString());
            file.close();
        } catch (IOException i) {
            return null;
        }
        return ResponseBuilder.getDungeonResponse(game);
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException, IOException {
        String gamePath = String.format("/saves/" + name + ".json");
        String gameFile = FileLoader.loadResourceFile(gamePath);
        JSONObject gameJSON = new JSONObject(gameFile);

        String configName = gameJSON.getString("config");

        GameBuilder builder = new GameBuilder();
        builder.setConfigName(configName);
        builder.setDungeonName(name);
        game = builder.buildGame(name, configName);

        return ResponseBuilder.getDungeonResponse(game);
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return FileLoader.listFileNamesInResourceDirectory("saves");
    }

    /**
     * /game/new/generate
     */
    public DungeonResponse generateDungeon(
            int xStart, int yStart, int xEnd, int yEnd, String configName) throws IllegalArgumentException {
        return null;
    }

    /**
     * /game/rewind
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        return null;
    }

}
