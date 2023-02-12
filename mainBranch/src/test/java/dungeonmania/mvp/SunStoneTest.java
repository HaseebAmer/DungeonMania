package dungeonmania.mvp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;


import static org.junit.jupiter.api.Assertions.*;

public class SunStoneTest {
    @Test
    public void sunStoneTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_updatedGoalsTest_treasure", "c_basicGoalsTest_treasure");
        assertEquals(":treasure", TestUtils.getGoals(res));
        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test building")
    public void testSunStoneBuilding() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        String config = "c_battleTests_shieldDurability";
        DungeonResponse res = controller.newGame("d_battleTest_buildingShieldSunStone", config);

        // Pick up Wood
        res = controller.tick(Direction.RIGHT);
        res = controller.tick(Direction.RIGHT);

        // Pick up treasure
        res = controller.tick(Direction.RIGHT);

        // Pick up key
        res = controller.tick(Direction.RIGHT);

        // Pick up sunstone
        res = controller.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        res = controller.build("shield");
        assertEquals(1, TestUtils.getInventory(res, "shield").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    }

    @Test
    @DisplayName("Testing if bribes ignore sunstones")
    public void bribeAmountSunStones() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_sunStoneBribe", "c_sunStoneTest_checkBribe");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

}
