package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssassinTest {

    @Test
    @Tag("12-1")
    @DisplayName("Test Assassin basic Movement")
    public void simpleMovementTest() {
        // 2 M Wall Wall
        // 1 Wall P2/ Tre P1 Wall Wall M2 M1
        // 0 P3 Wall Wall Wall
        // 1 2 3 4 5 6 7 8 9 10
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_as", "c_as");

        assertEquals(new Position(10, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(9, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(8, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(8, 2), getAssPos(res));
    }

    @Test
    @Tag("12-2")
    @DisplayName("Test Assassin Bribe Fail Rate ONE")
    public void failedBribeTest() {
        // 2 M Wall Wall
        // 1 Wall P2/ Tre P1 Wall Wall M2 M1
        // 0 P3 Wall Wall Wall
        // 1 2 3 4 5 6 7 8 9 10

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_as2", "c_as2");
        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        assertEquals(new Position(6, 1), getAssPos(res));
        res = dmc.tick(Direction.LEFT);

        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(4, 2), getAssPos(res));
        assertEquals(new Position(3, 2), getPlayerPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        // check enemies on map.

    }

    @Test
    @Tag("12-3")
    @DisplayName("Test Assassin Bribe Fail Rate ZERO")
    public void passedBribeTest() {
        // 2 M Wall Wall
        // 1 Wall P2/ Tre P1 Wall Wall M2 M1
        // 0 P3 Wall Wall Wall
        // 1 2 3 4 5 6 7 8 9 10

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_as3", "c_as3");
        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        assertEquals(new Position(6, 1), getAssPos(res));
        res = dmc.tick(Direction.LEFT);

        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        // if assassin walks into player, no battle.
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(4, 2), getAssPos(res));
        assertEquals(new Position(3, 2), getPlayerPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());

    }

    // test assassin successful bribe
    @Test
    @Tag("12-4")
    @DisplayName("Test Assassin Bribe Success and Follows Player")
    public void allyMovement() {
        // 2 M A1 Wall Wall
        // 1 Wall P2/ Tre P1 Wall Wall M2 M1
        // 0 P3 Wall Wall Wall
        // 1 2 3 4 5 6 7 8 9 10

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_as4", "c_as4");
        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        assertEquals(new Position(6, 2), getAssPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(5, 2), getAssPos(res));

        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(4, 1), getAssPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 1), getAssPos(res));

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 2), getAssPos(res));

    }

    private Position getAssPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "assassin").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }
}
