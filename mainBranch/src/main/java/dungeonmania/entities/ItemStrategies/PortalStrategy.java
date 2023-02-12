package dungeonmania.entities.ItemStrategies;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.map.GameMap;
import dungeonmania.entities.Portal;

public class PortalStrategy implements ItemStrategies {

    public PortalStrategy() {
    }

    @Override
    public void onOverlap(GameMap map, Entity entity1, Entity entity2) {
        if (entity1 instanceof Portal) {
            Portal portal = (Portal) entity1;
            if (portal.getPair() == null)
                return;
            if (entity2 instanceof Player || entity2 instanceof Mercenary || entity2 instanceof ZombieToast) {
                portal.doTeleport(map, entity2);
            }
        }
    }
}
