package dungeonmania.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.ItemStrategies.PlayerStrategy;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Overlap implements Battleable {
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 5.0;
    private BattleStatistics battleStatistics;
    private Inventory inventory;
    private Queue<Potion> queue = new LinkedList<>();
    private Potion inEffective = null;
    private int nextTrigger = 0;

    public Player(Position position, double health, double attack) {
        super(position, new PlayerStrategy());
        battleStatistics = new BattleStatistics(
                health,
                attack,
                0,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER);
        inventory = new Inventory();
    }

    public boolean hasWeapon() {
        return inventory.hasWeapon();
    }

    public boolean hasKey() {
        return inventory.hasKey();
    }

    public BattleItem getWeapon() {
        return inventory.getWeapon();
    }

    public List<String> getBuildables() {
        return inventory.getBuildables();
    }

    public boolean build(String entity, EntityFactory factory, Game game) {
        boolean remove = true;
        if (countEntityOfType(SunStone.class) >= 1) {
            remove = false;
        }
        InventoryItem item = inventory.checkBuildCriteria(this, remove, entity, factory);
        if (item == null)
            return false;

        if (item.getClass().equals(MidnightArmour.class)
                && game.countEntities(ZombieToast.class) != 0) {
            return false;
        } else if (item.getClass().equals(MidnightArmour.class)
                && game.countEntities(ZombieToast.class) == 0) {
            inventory.removeSwordSunstone();
        }
        return inventory.add(item);
    }

    public void move(GameMap map, Direction direction) {
        this.setFacing(direction);
        map.moveTo(this, Position.translateBy(this.getPosition(), direction));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public Entity getEntity(String itemUsedId) {
        return inventory.getEntity(itemUsedId);
    }

    public boolean pickUp(Entity item) {
        return inventory.add((InventoryItem) item);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Potion getEffectivePotion() {
        return inEffective;
    }

    public boolean hasKey(int number) {
        Key key = inventory.getFirst(Key.class);
        SunStone stone = inventory.getFirst(SunStone.class);
        return (key != null && key.getNumber() == number) || (stone != null);
    }

    public <T extends InventoryItem> void use(Class<T> itemType) {
        T item = inventory.getFirst(itemType);
        if (item != null && !(item instanceof SunStone))
            inventory.remove(item);
    }

    public void use(Bomb bomb, GameMap map) {
        inventory.remove(bomb);
        bomb.onPutDown(map, getPosition());
    }

    public void triggerNext(int currentTick) {
        if (queue.isEmpty()) {
            inEffective = null;
            return;
        }
        inEffective = queue.remove();
        applyBuff(battleStatistics);
        nextTrigger = currentTick + inEffective.getDuration();
    }

    public void use(Potion potion, int tick) {
        inventory.remove(potion);
        queue.add(potion);
        if (inEffective == null) {
            triggerNext(tick);
        }
    }

    public void use(Treasure treasure) {
        inventory.remove(treasure);
    }

    public void useWeapon() {
        inventory.useWeapon(this);
    }

    public void onTick(int tick) {
        if (inEffective == null || tick == nextTrigger) {
            triggerNext(tick);
        }
    }

    public void remove(InventoryItem item) {
        inventory.remove(item);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public double getHealth() {
        return battleStatistics.getHealth();
    }

    public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
        return inventory.count(itemType);
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        if (inEffective instanceof InvincibilityPotion) {
            return BattleStatistics.applyBuff(origin, new BattleStatistics(
                    0,
                    0,
                    0,
                    1,
                    1,
                    true,
                    true));
        } else if (inEffective instanceof InvisibilityPotion) {
            return BattleStatistics.applyBuff(origin, new BattleStatistics(
                    0,
                    0,
                    0,
                    1,
                    1,
                    false,
                    false));
        }
        return origin;
    }

    public int countGold() {
        return inventory.getGold();
    }

    public void bribe(int bribeAmount) {
        inventory.removeGold(bribeAmount, this);
    }

    public boolean isSceptreReady() {
        List<Sceptre> sceptres = getInventory().getEntities(Sceptre.class);
        if (sceptres.size() == 0)
            return false;
        Sceptre sceptre = sceptres.get(0);
        // player can use sceptre if it isn't in use currently.
        return !sceptre.getIsInControl();
    }

    public void commandSceptre(Mercenary mercenary) {
        mercenary.setMindControlled(true);
        List<Sceptre> sceptres = getInventory().getEntities(Sceptre.class);
        Sceptre sceptre = sceptres.get(0);
        sceptre.mindControl(mercenary);
    }

    public void addItem(InventoryItem item) {
        inventory.add(item);
    }

}
