package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MidnightArmourTest {

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    @Test
    @Tag("1-1")
    @DisplayName("Test midnight armour can be built")
    public void testBasicMidnightArmour() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_armor", "c_armor");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        // assertEquals(null, null);
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast").size());

        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
    }

    @Test
    @Tag("1-2")
    @DisplayName("Test midnight armour cannot be built without sun stone")
    public void testMidnightArmourCannotBeBuilt() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_armor", "c_armor");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("1-2")
    @DisplayName("Test midnight armour cannot be built with zombie condition")
    public void testMidnightArmourCannotBeBuiltZombie() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_armor2", "c_armor");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast").size());

        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("1-3")
    @DisplayName("Test midnight armour gives stats")
    public void testMidnightArmourStats() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_armor3", "c_armor3");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        // Check stats
        // res = dmc.tick(Direction.UP);
        // get battles as 1
        assertEquals(1, res.getBattles().size());
        // battle statistics
        // player: [health=10.0, attack=10.0 + 5.0, defence=0.0 + 5.0, magnifier=1.0,
        // reducer=10.0, invincible=false, enabled=true]
        // enemy: [health=5.0, attack=5.0, defence=0.0, magnifier=1.0, reducer=5.0,
        // invincible=false, enabled=true]

        BattleResponse battle = res.getBattles().get(0);

        RoundResponse firstRound = battle.getRounds().get(0);
        // Delta health is negative so take negative here
        // 5-4 / 10.0 = 0.1
        assertEquals(0.1, -firstRound.getDeltaCharacterHealth(), 0.001);
    }
}
