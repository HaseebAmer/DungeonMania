package dungeonmania.mvp.Goals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class BasicEnemyGoalsTest {
    @Test
    public void testEnemySpiderGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalsTest_simpleSpider", "c_complexGoalsTest_andAll");
        assertEquals(":enemies", TestUtils.getGoals(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    public void testEnemySpawnerGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoal_simpleSpawner", "c_battleTest_noWinners");
        assertEquals(":enemies", TestUtils.getGoals(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        String spawnerID = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        res = assertDoesNotThrow(() -> dmc.interact(spawnerID));
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    public void testMultipleEnemySpiderGoalMet() {

        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalsTest_manyEnemies", "c_complexGoalsTest_andAll");
        assertEquals(":enemies", TestUtils.getGoals(res));

        // kill spider
        assertEquals(3, TestUtils.getEntities(res, "spider").size());
        res = dmc.tick(Direction.RIGHT);

        assertEquals(2, TestUtils.getEntities(res, "spider").size());
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    public void testMultipleEnemySpiderGoalNotMet() {

        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalsTest_manyEnemies", "c_complexGoalsTest_moreEnemies");

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        // kill spider
        assertEquals(3, TestUtils.getEntities(res, "spider").size());
        res = dmc.tick(Direction.RIGHT);

        assertEquals(2, TestUtils.getEntities(res, "spider").size());
        assertEquals(":enemies", TestUtils.getGoals(res));
    }

}
