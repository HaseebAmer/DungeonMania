package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean allied = false;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     *
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {

        if (player.isSceptreReady())
            return true;

        if (player.countGold() < bribeAmount) {
            return false;
        }

        int x = getPosition().getX();
        int y = getPosition().getY();

        int a = player.getPosition().getX();
        int b = player.getPosition().getY();

        int distance = Math.abs(a - x) + Math.abs(b - y);

        return distance > 0 && distance <= 2 * bribeRadius;
    }

    /**
     * bribe the merc
     */
    public void bribe(Player player) {
        if (!player.isSceptreReady()) {
            player.bribe(bribeAmount);
        } else {
            player.commandSceptre(this);
        }
    }

    @Override
    public void interact(Player player, Game game) {
        allied = true;
        bribe(player);
    }

    public void setMindControlled(boolean side) {
        this.allied = side;
    }

    @Override
    public void move(Game game) {
        applySpawnedOnSwampDebuff(game);
        if (!getMovementState().canMove()) {
            return;
        }
        Position nextPos;
        GameMap map = game.getMap();
        nextPos = map.dijkstraPathFind(getPosition(), map.getPlayerPosition(), this);
        Position prevPos = map.getPreviousPosition();
        Position currPos = map.getPlayerPosition();

        if (comparePositions(prevPos, currPos)) {
            List<Position> adjacentPositions = currPos.getAdjacentPositions();
            for (Position pos : adjacentPositions) {
                if (comparePositions(getPosition(), pos))
                    return;
            }
            map.moveTo(this, nextPos);
            return;
        }

        List<Position> prev = prevPos.getAdjacentPositions();
        prev.add(prevPos);
        for (Position pos : prev) {
            if (comparePositions(getPosition(), pos) && !comparePositions(prevPos, currPos)) {
                map.moveTo(this, prevPos);
                return;
            }
        }
        map.moveTo(this, nextPos);
    }

    @Override
    public boolean isInteractable(Player player) {
        if (!allied && canBeBribed(player)) {
            return true;
        }
        return false;
    }
}
