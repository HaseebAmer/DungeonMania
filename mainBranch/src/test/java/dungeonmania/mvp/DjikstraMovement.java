package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// import javax.naming.InitialContext;

public class DjikstraMovement {

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    @Test
    @Tag("1-1")
    @DisplayName("Test ally mercenary utilises dijkstra movement")
    public void testAllyDijkstraMovement() {
        // 2 M Wall Wall
        // 1 Wall P2/ Tre P1 Wall Wall M2 M1
        // 0 P3 Wall Wall Wall
        // 1 2 3 4 5 6 7 8 9 10
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame(
                "d_DjikstraMovement_basic", "c_DjikstraMovement_basic");
        EntityResponse initPlayer = TestUtils.getPlayer(initDungonRes).get();

        // c_movementTest_testMovementDown
        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(),
                new Position(3, 1), false);

        assertEquals(new Position(10, 1), getMercPos(initDungonRes));

        // move player left
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse actualPlayer = TestUtils.getPlayer(actualDungonRes).get();

        // assert player does move left
        assertTrue(TestUtils.entityResponsesEqual(expectedPlayer, actualPlayer));
        // assert m1 moves to m2 after movement.
        assertEquals(new Position(9, 1), getMercPos(actualDungonRes));

        assertEquals(1, TestUtils.getInventory(actualDungonRes, "treasure").size());

        String mercId = TestUtils.getEntitiesStream(actualDungonRes, "mercenary").findFirst().get().getId();

        // achieve bribe
        actualDungonRes = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(actualDungonRes, "treasure").size());

        assertEquals(new Position(8, 1), getMercPos(actualDungonRes));

        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(8, 2), getMercPos(actualDungonRes));

        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(7, 2), getMercPos(actualDungonRes));

        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(6, 2), getMercPos(actualDungonRes));

        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(5, 2), getMercPos(actualDungonRes));

        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 2), getMercPos(actualDungonRes));

        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 2), getMercPos(actualDungonRes));

        actualDungonRes = dmc.tick(Direction.UP);
        assertEquals(new Position(3, 1), getMercPos(actualDungonRes));

        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 0), getMercPos(actualDungonRes));
    }

    @Test
    @Tag("1-2")
    @DisplayName("Test ally mercenary sticking to Player")
    public void testAllySticking() {
        // 2 PF MF M1 Wall Wall
        // 1 Wall P2/ Tre P1 Wall Wall
        // 0 P3 Wall Wall Wall
        // 1 2 3 4 5 6 7 8 9 10
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame(
                "d_DjikstraMovement_sticking", "c_DjikstraMovement_sticking");
        EntityResponse initPlayer = TestUtils.getPlayer(initDungonRes).get();

        // c_movementTest_testMovementDown
        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(),
                new Position(3, 1), false);

        assertEquals(new Position(5, 2), getMercPos(initDungonRes));

        // move player left
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse actualPlayer = TestUtils.getPlayer(actualDungonRes).get();

        // assert player does move left
        assertTrue(TestUtils.entityResponsesEqual(expectedPlayer, actualPlayer));
        // assert m1 moves to m2 after movement.
        assertEquals(new Position(4, 1), getMercPos(actualDungonRes));

        assertEquals(1, TestUtils.getInventory(actualDungonRes, "treasure").size());

        String mercId = TestUtils.getEntitiesStream(actualDungonRes, "mercenary").findFirst().get().getId();

        // achieve bribe
        actualDungonRes = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(actualDungonRes, "treasure").size());

        actualDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 1), getMercPos(actualDungonRes));
        assertTrue(Position.isAdjacent(getMercPos(actualDungonRes), TestUtils.getPlayerPos(actualDungonRes)));

        actualDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 2), getMercPos(actualDungonRes));

        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 2), getMercPos(actualDungonRes));
        assertTrue(Position.isAdjacent(getMercPos(actualDungonRes), TestUtils.getPlayerPos(actualDungonRes)));

        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 2), getMercPos(actualDungonRes));
    }
}
