package casm.Utils;

import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.Components.Collision.Rectangle;
import casm.ECS.Components.PositionComponent;
import casm.Map.Map;

public class CollisionUtils {

    private static CollisionUtils instance = null;

    private CollisionUtils() {
    }

    public static CollisionUtils getInstance() {
        if (instance == null)
            instance = new CollisionUtils();
        return instance;
    }

    /**
     * If the entity has collision on the X axe,
     * it will move it to the tile edge to don't have wired space between the entity and the tile collided to
     *
     * @param positionComponent the entity position component({@link PositionComponent})
     * @param collider          entity collider rectangle({@link Rectangle})
     */
    public void moveToTileEdgeX(PositionComponent positionComponent, Rectangle collider) {
        if (positionComponent.sign.x > 0) {
            double oldPosition = positionComponent.position.x;
            positionComponent.position.x = ((int) (collider.getX() / Map.getTileWidth()) + 1) * Map.getTileWidth()
                    - collider.getWidth() - collider.getOffset().x - 1 + oldPosition;
            positionComponent.position.x += oldPosition - positionComponent.position.x;
            //TODO: Trebuie reparat BugFix-ul asta: il folosim deoare ce AiBehaviour vedem daca avem collziune pe o parte si daca da mergem in cealata parte, dar asa noi il lipim fix langa tile fara coliziune(ceea ce vrem pentru player)
            if (positionComponent.gameObject.getName().equals("enemy")) {
                positionComponent.position.x += 1;
            }
        }
        if (positionComponent.sign.x < 0) {
            double oldPosition = positionComponent.position.x;
            positionComponent.position.x = (int) (collider.getX() / Map.getTileWidth()) * Map.getTileWidth() - collider.getOffset().x;
            positionComponent.position.x += oldPosition - positionComponent.position.x;
            if (positionComponent.gameObject.getName().equals("enemy"))
                positionComponent.position.x -= 1;
        }
    }

    /**
     * If the entity has collision on the Y axe,
     * it will move it to the tile edge to don't have wired space between the entity and the tile collided to
     *
     * @param positionComponent the entity position component({@link PositionComponent})
     * @param collider          entity collider rectangle({@link Rectangle})
     */
    public void moveToTileEdgeY(PositionComponent positionComponent, Rectangle collider) {
        if (positionComponent.velocity.y < 0) {
            if (!positionComponent.isClimbing()) {
                positionComponent.position.y = (int) (collider.getY() / Map.getTileHeight()) * Map.getTileHeight() + 1 - collider.getOffset().y;

                positionComponent.sign.y = 0;
                positionComponent.velocity.y = 0;
                positionComponent.setJumping(false);
            }
        } else {
            positionComponent.position.y = ((int) ((collider.getY() + collider.getHeight()) / Map.getTileHeight()) + 1)
                    * Map.getTileHeight() - collider.getHeight() - 1 - collider.getOffset().y;
        }
    }

    /**
     * Verify if the entity has any collection of 'type' in its collider flags
     *
     * @param collisionCornersFlags the collider flags form {@link DyncamicColliderComponent}
     * @param type                  the type of the searched collider
     * @return if it was found or not
     */
    private boolean hasCollisionOfType(ColliderType[] collisionCornersFlags, ColliderType type) {
        for (ColliderType flagType : collisionCornersFlags)
            if (flagType == type)
                return true;
        return false;
    }

    /**
     * Verify if the entity has any collection of 'type' in the bottom of the entity, searching in its collider flags
     *
     * @param collisionCornersFlags the collider flags form {@link DyncamicColliderComponent}
     * @param type                  the type of the searched collider
     * @return if it was found or not
     */
    public boolean hasCollisionOfTypeOnBottom(ColliderType[] collisionCornersFlags, ColliderType type) {
        return collisionCornersFlags[2] == type || collisionCornersFlags[3] == type;
    }

    /**
     * Verify if the entity has any collection of 'type' in the middle of the entity, searching in its collider flags
     *
     * @param collisionCornersFlags the collider flags form {@link DyncamicColliderComponent}
     * @param type                  the type of the searched collider
     * @return if it was found or not
     */
    private boolean hasCollisionOfTypeOnMiddle(ColliderType[] collisionCornersFlags, ColliderType type) {
        return collisionCornersFlags[4] == type || collisionCornersFlags[5] == type;
    }


    /**
     * Returns a bite codifiyed number with the collision variables.<br>
     * It is a number of 4 bites (1111)<br>
     * 1 bite -> If there is any collision with the map tiles<br>
     * 2 bite -> If there is collision with a leader in the middle points<br>
     * 3 bite -> If there is collision with a leader in the bottom points<br>
     * 4 bite -> If there is any collision with lava
     * @param dynamicColl the collider on which it will be tested
     * @return collision codification
     */
    public byte getCollisionVariable(DyncamicColliderComponent dynamicColl) {
        byte result = 0b0000;
        // Collision with the map
        result = hasCollisionOfType(dynamicColl.getCollisionCornersFlags(), ColliderType.MAP_TILE)
                ? (byte) (result | 0b0001) : result;
        // Middle point collision with leader
        result = (hasCollisionOfTypeOnMiddle(dynamicColl.getCollisionCornersFlags(), ColliderType.LEADER) ||
                hasCollisionOfTypeOnMiddle(dynamicColl.getCollisionCornersFlags(), ColliderType.LAVA))
                ? (byte) (result | 0b0010) : result;
        // Bottom point collision with leader
        result = (hasCollisionOfTypeOnBottom(dynamicColl.getCollisionCornersFlags(), ColliderType.LEADER) ||
                hasCollisionOfTypeOnBottom(dynamicColl.getCollisionCornersFlags(), ColliderType.LAVA))
                ? (byte) (result | 0b0100) : result;
        // Any collision with lava
        result = hasCollisionOfTypeOnMiddle(dynamicColl.getCollisionCornersFlags(), ColliderType.LAVA)
                ? (byte) (result | 0b1000) : result;

        return result;
    }
}
