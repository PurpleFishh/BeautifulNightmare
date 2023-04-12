package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.GameObject;
import casm.Map.Map;
import casm.Utils.Vector2D;

import java.util.List;

public class DyncamicColliderComponent extends Component {

    private boolean onGround = false;
    /**
     *
     */
    private boolean[] collisionCorners;

    @Override
    public void init() {
        collisionCorners = new boolean[6];
    }

    public boolean checkCollision(Vector2D position, int width, int height, List<GameObject> testEntities, int testWidth, int testHeight) {
        collisionCorners[0] = IsSolid(position.x, position.y, testEntities, testWidth, testHeight);
        collisionCorners[1] = IsSolid(position.x + width, position.y, testEntities, testWidth, testHeight);
        collisionCorners[2] = IsSolid(position.x, position.y + height, testEntities, testWidth, testHeight);
        collisionCorners[3] = IsSolid(position.x + width, position.y + height, testEntities, testWidth, testHeight);
        /// Because the player is taller than a tile and if you jump the collider could be between the coroners
        collisionCorners[4] = IsSolid(position.x, position.y + (double) height / 2, testEntities, testWidth, testHeight);
        collisionCorners[5] = IsSolid(position.x + width, position.y + (double) height / 2, testEntities, testWidth, testHeight);
        return (collisionCorners[0] || collisionCorners[1] || collisionCorners[2] || collisionCorners[3] ||
                collisionCorners[4] || collisionCorners[5]);
    }

    private static boolean IsSolid(double x, double y, List<GameObject> testEntities, int testWidth, int testHeight) {
        if (x < 0 || x >= Map.getCols() * Map.getTileWidth()) return true;
        if (y < 0 || y >= Map.getRows() * Map.getTileHeight()) return true;

        int xIndex = (int) (x / testWidth);
        int yIndex = (int) (y / testHeight);
        return testEntities.get(yIndex * Map.getCols() + xIndex).getComponent(ColliderComponent.class) != null;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean[] getCollisionCorrnersFlags() {
        return collisionCorners;
    }
}
