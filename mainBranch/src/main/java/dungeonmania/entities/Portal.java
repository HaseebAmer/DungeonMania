package dungeonmania.entities;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.ItemStrategies.PortalStrategy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Portal extends Overlap {
    private ColorCodedType color;
    private Portal pair;

    public Portal(Position position, ColorCodedType color) {
        super(position, new PortalStrategy());
        this.color = color;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (pair == null)
            return false;
        if (entity instanceof Player || entity instanceof Mercenary)
            return pair.canTeleportTo(map, entity);
        return true;
    }

    public boolean canTeleportTo(GameMap map, Entity entity) {
        List<Position> neighbours = getCardinallyAdjacentPositions();
        return neighbours.stream().allMatch(n -> map.canMoveTo(entity, n));
    }

    public Portal getPair() {
        return pair;
    }

    public void doTeleport(GameMap map, Entity entity) {
        Position destination = pair.getCardinallyAdjacentPositions()
                .stream()
                .filter(dest -> map.canMoveTo(entity, dest))
                .findAny()
                .orElse(null);
        if (destination != null) {
            map.moveTo(entity, destination);
        }
    }

    public String getColor() {
        return color.toString();
    }

    public List<Position> getDestPositions(GameMap map, Entity entity) {
        return pair == null
                ? null
                : pair.getAdjacentPositions()
                        .stream()
                        .filter(p -> map.canMoveTo(entity, p))
                        .collect(Collectors.toList());
    }

    public void bind(Portal portal) {
        if (this.pair == portal)
            return;
        if (this.pair != null) {
            this.pair.bind(null);
        }
        this.pair = portal;
        if (portal != null) {
            portal.bind(this);
        }
    }

}
