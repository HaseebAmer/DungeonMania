package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {

        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        int treasure = count(Treasure.class);
        int sword = count(Sword.class);

        List<InventoryItem> treasureExclusive = items.stream()
                .filter(it -> it.getClass().equals(Treasure.class))
                .collect(Collectors.toList());

        int t = treasureExclusive.size();
        int keys = count(Key.class);
        int sunstones = count(SunStone.class);
        List<String> result = new ArrayList<>();

        if (wood >= 1 && arrows >= 3) {
            result.add("bow");
        }
        if (wood >= 2 && (sunstones >= 1 || (treasure >= 1 || keys >= 1))) {
            result.add("shield");
        }
        if ((wood >= 1 || arrows >= 2) && (keys >= 1 || t >= 1) && sunstones >= 1) {
            result.add("sceptre");
        }

        if (sword >= 1 && sunstones >= 1) {
            result.add("midnight_armour");
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(Player p, boolean remove, String buildable, EntityFactory factory) {

        List<Wood> wood = getEntities(Wood.class);
        List<Arrow> arrows = getEntities(Arrow.class);
        List<Treasure> treasure = getEntities(Treasure.class);
        List<SunStone> stones = getEntities(SunStone.class);
        List<Key> keys = getEntities(Key.class);
        List<SunStone> sunstones = getEntities(SunStone.class);
        List<Sword> sword = getEntities(Sword.class);

        List<InventoryItem> treasureExclusive = items.stream()
                .filter(it -> it.getClass().equals(Treasure.class))
                .collect(Collectors.toList());

        if (canBuildBow(wood, arrows, buildable)) {
            items.remove(wood.get(0));
            items.remove(arrows.get(0));
            items.remove(arrows.get(1));
            items.remove(arrows.get(2));
            return factory.buildBow();

        } else if (canBuildShield(wood, keys, treasure, buildable)) {
            if (remove) {
                if (treasure.size() >= 1) {
                    items.remove(treasure.get(0));
                } else {
                    items.remove(keys.get(0));
                }
            }
            items.remove(wood.get(0));
            items.remove(wood.get(1));
            return factory.buildShield();

        } else if (canBuildSceptre(wood, arrows, stones, keys)) {
            if (wood.size() >= 1) {
                items.remove(wood.get(0));
            } else {
                items.remove(arrows.get(0));
                items.remove(arrows.get(1));
            }

            if (keys.size() >= 1) {
                items.remove(keys.get(0));
            } else {
                items.remove(treasureExclusive.get(0));
            }

            items.remove(stones.get(0));
            return factory.buildSceptre();
        } else if (canBuildMidnightArmor(sword, sunstones, buildable)) {
            return factory.buildMidnightArmour();
        }
        return null;
    }

    public void removeSwordSunstone() {
        List<SunStone> sunstones = getEntities(SunStone.class);
        List<Sword> sword = getEntities(Sword.class);
        items.remove(sword.get(0));
        items.remove(sunstones.get(0));
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public int getGold() {
        int gold = count(Treasure.class);
        for (InventoryItem item : items) {
            if (item instanceof SunStone) {
                gold--;
            }
        }
        return gold;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null) {
            weapon = getFirst(Bow.class);
        }

        if (weapon == null) {
            weapon = getFirst(Shield.class);
        }

        return weapon;
    }

    public void useWeapon(Player player) {
        getWeapon().use(player);
    }

    public boolean canBuildBow(List<Wood> wood, List<Arrow> arrows, String buildable) {
        return wood.size() >= 1 && arrows.size() >= 3 && buildable.equals("bow");
    }

    public boolean canBuildShield(List<Wood> wood, List<Key> keys,
            List<Treasure> treasure, String buildable) {
        return wood.size() >= 2
                && (treasure.size() >= 1 || keys.size() >= 1)
                && buildable.equals("shield");
    }

    public boolean canBuildSceptre(List<Wood> wood, List<Arrow> arrows, List<SunStone> stones,
            List<Key> keys) {
        List<InventoryItem> treasure = items.stream()
                .filter(it -> it.getClass().equals(Treasure.class))
                .collect(Collectors.toList());

        return (wood.size() >= 1 || arrows.size() >= 2)
                && (keys.size() >= 1 || treasure.size() >= 1) && stones.size() >= 1;
    }

    public boolean canBuildMidnightArmor(List<Sword> sword, List<SunStone> sunstones, String buildable) {
        return sword.size() >= 1 && sunstones.size() >= 1 && buildable.equals("midnight_armour");
    }

    public void removeGold(int bribeAmount, Player player) {
        int gold = 0;
        List<Treasure> treasures = getEntities(Treasure.class);
        for (InventoryItem item : treasures) {
            if (gold == bribeAmount) {
                return;
            }
            if (!(item instanceof SunStone)) {
                player.use((Treasure) item);
                gold++;
            }
        }
    }

    public boolean hasKey() {
        int keys = count(Key.class);
        return keys > 0;
    }

}
