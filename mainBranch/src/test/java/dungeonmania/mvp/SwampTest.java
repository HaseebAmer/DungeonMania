package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SwampTest {

    @Test
    public void swampSlowsMercenaryAndSpider() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swamp1", "c_spectre2");
        assertEquals(new Position(-5, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        assertEquals(new Position(-4, 2), TestUtils.getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT); // move on
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT); // off
        assertEquals(new Position(-3, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        assertEquals(new Position(-3, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT); // walks as normal
        assertEquals(new Position(-2, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        assertEquals(new Position(-2, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    public void swampDoesNotSlowPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swamp2", "c_spectre2");
        assertEquals(new Position(1, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(0, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(-1, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    public void swampSlowsSingleEnemy() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swamp5", "c_spectre3");
        assertEquals(new Position(-5, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // moved on
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // off
        assertEquals(new Position(-3, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    public void multipleSwampTiles() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swamp4", "c_spectre3");
        res = dmc.tick(Direction.RIGHT); // moved on
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "assassin").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "assassin").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-4, 1), TestUtils.getEntities(res, "assassin").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // moved onto second swamp tile
        assertEquals(new Position(-3, 1), TestUtils.getEntities(res, "assassin").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-3, 1), TestUtils.getEntities(res, "assassin").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-3, 1), TestUtils.getEntities(res, "assassin").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT); // off
        assertEquals(new Position(-2, 1), TestUtils.getEntities(res, "assassin").get(0).getPosition());
    }

    @Test
    public void adjacentAlliesDontGetStuck() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swamp3", "c_spectre3");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(0, 1), getMercPos(res));
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertTrue(Position.isAdjacent(getMercPos(res), TestUtils.getPlayerPos(res)));
        res = dmc.tick(Direction.RIGHT); // step into swamp tile.
        res = dmc.tick(Direction.RIGHT); // step out of swamp tile, unaffected.
        assertEquals(new Position(4, 1), TestUtils.getPlayerPos(res));
        assertEquals(new Position(3, 1), getMercPos(res));
    }

    @Test
    public void testAlliesStuck() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swamp6", "c_spectre3");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(-1, 1), getMercPos(res));
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(new Position(0, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT); // move on tile
        assertEquals(new Position(3, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT); // stuck
        res = dmc.tick(Direction.RIGHT); // stuck
        res = dmc.tick(Direction.RIGHT); // move off
        assertEquals(new Position(4, 1), getMercPos(res));
        assertEquals(new Position(8, 1), TestUtils.getPlayerPos(res)); // player wasnt stuck.
    }

    @Test
    public void enemiesSpawnOnSwampTile() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swamp7", "c_spectre3");
        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-2, 1), getMercPos(res));
        assertEquals(new Position(-2, 1), getSpiderPos(res));
        res = dmc.tick(Direction.RIGHT); // stuck
        assertEquals(new Position(-2, 1), getMercPos(res));
        assertEquals(new Position(-2, 1), getSpiderPos(res));
        res = dmc.tick(Direction.RIGHT); // off
        assertEquals(new Position(-1, 1), getMercPos(res));
        assertEquals(new Position(-2, 0), getSpiderPos(res));

    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    private Position getSpiderPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "spider").get(0).getPosition();
    }

}
