package dungeonmania.mvp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class PersistenceTest {
    @Test
    public void persistenceTest() throws IllegalArgumentException, IOException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
                "d_persistence", "c_sceptreTest1");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        DungeonResponse save = dmc.saveGame("save2");
        String savedGoals = save.getGoals();

        DungeonResponse load = dmc.loadGame("save2");
        assertEquals(1, TestUtils.getInventory(load, "sceptre").size());
        assertTrue(TestUtils.entityListEqual(TestUtils.getEntities(save), TestUtils.getEntities(load)));
        assertTrue(TestUtils.itemListEqual(TestUtils.getInventory(save, ""), TestUtils.getInventory(load, "")));
        assertEquals(TestUtils.getGoals(save), TestUtils.getGoals(load));
    }

}
