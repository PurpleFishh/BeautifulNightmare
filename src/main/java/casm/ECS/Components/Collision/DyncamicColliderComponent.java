package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.PositionComponent;
import casm.ECS.GameObject;
import casm.Game;
import casm.Map.Map;
import casm.Objects.Entities.Tile;
import casm.Utils.Vector2D;

import java.util.List;

/**
 * Component that is used
 * to update the flags for collision with a given set of game objects(especially for collision with the map)
 */
public class DyncamicColliderComponent extends Component {

    /**
     * <b>onGround</b> - flag if the entity is on ground
     * <b>onLeader</b> - flag if the entity is on leader
     */
    private boolean onGround = false, onLeader = false;
    /**
     * collisionCorners is an array of flags that specify if it has a collision on an edge, what type of collider it is<br>
     * Array indexing:
     * <ul>
     * <li>0 - left top corner</li>
     * <li>1 - right top corner</li>
     * <li>2 - left button corner</li>
     * <li>3 - right button corner</li>
     * <li>4 - left middle</li>
     * <li>5 - right middle</li>
     * </ul>
     */
    private ColliderType[] collisionCorners;

    /**
     * Initializing {@link DyncamicColliderComponent}
     */
    @Override
    public void init() {
        collisionCorners = new ColliderType[6];
    }

    /**
     * Check collision for a set of entities, it will update the flags of collisionCorners
     *
     * @param position     the position of the entity
     * @param width        entity width
     * @param height       entity height
     * @param testEntities the set of entity what will be checked collision with
     * @param testWidth    the test entities width
     * @param testHeight   the test entities height
     */
    public void checkCollision(Vector2D position, int width, int height, List<GameObject> testEntities, int testWidth, int testHeight) {
        collisionCorners[0] = IsSolid(position.x, position.y, testEntities, testWidth, testHeight);
        collisionCorners[1] = IsSolid(position.x + width, position.y, testEntities, testWidth, testHeight);
        collisionCorners[2] = IsSolid(position.x, position.y + height, testEntities, testWidth, testHeight);
        collisionCorners[3] = IsSolid(position.x + width, position.y + height, testEntities, testWidth, testHeight);
        /// Because the player is taller than a tile and if you jump, the collider could be between the coroners
        collisionCorners[4] = IsSolid(position.x, position.y + (double) height / 2, testEntities, testWidth, testHeight);
        collisionCorners[5] = IsSolid(position.x + width, position.y + (double) height / 2, testEntities, testWidth, testHeight);
    }

    /**
     * Check if the gave position is in collision with something
     *
     * @param x            x position
     * @param y            y position
     * @param testEntities the set of entity what will be checked collision with
     * @param testWidth    the test entities width
     * @param testHeight   the test entities height
     * @return the collider type in collision with or null if there is no collision
     */
    private static ColliderType IsSolid(double x, double y, List<GameObject> testEntities, int testWidth, int testHeight) {
        if (x < 0 || x >= Map.getCols() * Map.getTileWidth()) return ColliderType.MAP_TILE;
        if (y < 0) return ColliderType.MAP_TILE;
        if (y >= Map.getRows() * Map.getTileHeight()) return ColliderType.VOID;

        int xIndex = (int) (x / testWidth);
        int yIndex = (int) (y / testHeight);
        if (testEntities != null) {
            if (Game.getLevelScene().getLevel() != 1) {
                if (testEntities.get(yIndex * Map.getCols() + xIndex).hasComponent(ColliderComponent.class))
                    return testEntities.get(yIndex * Map.getCols() + xIndex).getComponent(ColliderComponent.class).getType();
            } else {
                for (GameObject testObj : testEntities) {
                    if (testObj.getComponent(PositionComponent.class).position.x == xIndex * testWidth &&
                            testObj.getComponent(PositionComponent.class).position.y == yIndex * testHeight) {
                        if (testObj.hasComponent(ColliderComponent.class)) {
                            return testObj.getComponent(ColliderComponent.class).getType();
                        }
                        //break;
                    }
                }
            }
        }

        return null;
    }

    /**
     * @return get if the entity is on the ground
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Set if the entity is on the ground or not
     *
     * @param onGround is on the ground or not
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    /**
     * @return the collider corners array flags
     */
    public ColliderType[] getCollisionCornersFlags() {
        return collisionCorners;
    }

    /**
     * @return get if the entity is on leader
     */
    public boolean isOnLeader() {
        return onLeader;
    }

    /**
     * Set if the entity is on leader or not
     *
     * @param onLeader is on the leader or not
     */
    public void setOnLeader(boolean onLeader) {
        this.onLeader = onLeader;
    }
}
