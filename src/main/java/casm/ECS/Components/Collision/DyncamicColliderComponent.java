package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.GameObject;
import casm.Map.Map;
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
     * <li>0 - left top corner<br>
     * <li>1 - right top corner<br>
     * <li>2 - left button corner<br>
     * <li>3 - right button corner<br>
     * <li>4 - left middle<br>
     * <li>5 - right middle<br>
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
        if (y < 0 || y >= Map.getRows() * Map.getTileHeight()) return ColliderType.MAP_TILE;

        int xIndex = (int) (x / testWidth);
        int yIndex = (int) (y / testHeight);
        if (testEntities.get(yIndex * Map.getCols() + xIndex).hasComponent(ColliderComponent.class))
            return testEntities.get(yIndex * Map.getCols() + xIndex).getComponent(ColliderComponent.class).getType();
        else
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
     * @param onLeader is on the leader or not
     */
    public void setOnLeader(boolean onLeader) {
        this.onLeader = onLeader;
    }
}
