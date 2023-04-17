package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.GameObject;
import casm.Map.Map;
import casm.Utils.Vector2D;

import java.util.List;

public class DyncamicColliderComponent extends Component {

    private boolean onGround = false;
    /**
     *
     */
    private ColliderType[] collisionCorners;

    // TODO: Sa adaug la fiecare collider un tag care semnifica tipul colliderului ca ENUM
    // TODO: Facuta remediera collisiunlor ca un Mediator primind DynColl si PosComp si el sa vada ce fel de coliziune e cu cine ce tre sa faca si cum se rezolva si ii da inapoi lui PosComp noua pozitie si actualizeaza Flagurile
    // TODO: collsionCorners va contine tipul collisiuni sau ref la Collider

    @Override
    public void init() {
        collisionCorners = new ColliderType[6];
    }

    public boolean checkCollision(Vector2D position, int width, int height, List<GameObject> testEntities, int testWidth, int testHeight) {
        collisionCorners[0] = IsSolid(position.x, position.y, testEntities, testWidth, testHeight);
        collisionCorners[1] = IsSolid(position.x + width, position.y, testEntities, testWidth, testHeight);
        collisionCorners[2] = IsSolid(position.x, position.y + height, testEntities, testWidth, testHeight);
        collisionCorners[3] = IsSolid(position.x + width, position.y + height, testEntities, testWidth, testHeight);
        /// Because the player is taller than a tile and if you jump the collider could be between the coroners
        collisionCorners[4] = IsSolid(position.x, position.y + (double) height / 2, testEntities, testWidth, testHeight);
        collisionCorners[5] = IsSolid(position.x + width, position.y + (double) height / 2, testEntities, testWidth, testHeight);
        return (collisionCorners[0] != null || collisionCorners[1] != null || collisionCorners[2] != null || collisionCorners[3] != null ||
                collisionCorners[4] != null || collisionCorners[5] != null);
    }

    private static ColliderType IsSolid(double x, double y, List<GameObject> testEntities, int testWidth, int testHeight) {
        if (x < 0 || x >= Map.getCols() * Map.getTileWidth()) return ColliderType.MAP_TILE;
        if (y < 0 || y >= Map.getRows() * Map.getTileHeight()) return ColliderType.MAP_TILE;

        int xIndex = (int) (x / testWidth);
        int yIndex = (int) (y / testHeight);
        if(testEntities.get(yIndex * Map.getCols() + xIndex).hasComponent(ColliderComponent.class))
            return testEntities.get(yIndex * Map.getCols() + xIndex).getComponent(ColliderComponent.class).getType();
        else
            return null;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public ColliderType[] getCollisionCorrnersFlags() {
        return collisionCorners;
    }
}
