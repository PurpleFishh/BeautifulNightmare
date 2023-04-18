package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.AnimationStateMachine;
import casm.ECS.Components.KeyboardControllerComponent;
import casm.ECS.Components.PositionComponent;
import casm.ECS.GameObject;
import casm.Game;
import casm.Map.Map;
import casm.Utils.Mediator;
import casm.Utils.Vector2D;

public class MovementMediator implements Mediator {
    @Override
    public void notify(Component component) {
        if (component.gameObject.hasComponent(KeyboardControllerComponent.class))
            keysMovementManager((KeyboardControllerComponent) component);
        if (component.gameObject.hasComponent(DyncamicColliderComponent.class))
            collisionManager((PositionComponent) component);
        else
            positionUpdate((PositionComponent) component);
    }

    private void collisionManager(PositionComponent positionComponent) {
        GameObject gameObject = positionComponent.gameObject;
        DyncamicColliderComponent dynamicColl = gameObject.getComponent(DyncamicColliderComponent.class);
        Vector2D potentialPosition = positionComponent.getPotentialPosition();

        Vector2D xPot = new Vector2D(potentialPosition.x, positionComponent.position.y);
        Vector2D yPot = new Vector2D(positionComponent.position.x, potentialPosition.y);

        int width = gameObject.getComponent(ColliderComponent.class).getRectWidth();
        int height = gameObject.getComponent(ColliderComponent.class).getRectHeight();

        /// Check for Y movement
        dynamicColl.checkCollision(yPot, width, height,
                Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());

        boolean yLeader = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.LEADER);
        boolean collisionY = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.MAP_TILE);
        if (collisionY)
            moveToTileEdgeY(positionComponent);
        else
            positionComponent.position.y = yPot.y;
//        if (!collisionY) {
//
//            yPot.y += 1;
//            dynamicColl.checkCollision(yPot, width, height,
//                    Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
//            collisionY = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.MAP_TILE);
//            if (!collisionY) {
//                yPot.y += 1;
//                dynamicColl.checkCollision(yPot, width, height,
//                        Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
//                collisionY = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.MAP_TILE);
//                if (collisionY)
//                    positionComponent.position.y = yPot.y - 1;
//                else
//                    positionComponent.position.y = yPot.y - 2;
//            } else
//                positionComponent.position.y = yPot.y - 1;
//
//        }
        dynamicColl.setOnGround(dynamicColl.getCollisionCorrnersFlags()[2] == ColliderType.MAP_TILE || dynamicColl.getCollisionCorrnersFlags()[3] == ColliderType.MAP_TILE);

        /// Check for X movement
        dynamicColl.checkCollision(xPot, width, height,
                Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
        boolean collisionX = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.MAP_TILE);
        if (collisionX) {
            moveToTileEdgeX(positionComponent);
        } else
            positionComponent.position.x = xPot.x;
//        if (!collisionX) {
//
//            xPot.x += 1;
//            dynamicColl.checkCollision(xPot, width, height,
//                    Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
//            collisionX = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.MAP_TILE);
//            if (!collisionX) {
//                xPot.x += 1;
//                dynamicColl.checkCollision(xPot, width, height,
//                        Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
//                collisionX = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.MAP_TILE);
//                if (collisionX)
//                    positionComponent.position.x = xPot.x - 1;
//                else
//                    positionComponent.position.x = xPot.x - 2;
//            } else
//                positionComponent.position.x = xPot.x - 1;
//        }
        dynamicColl.setOnLeader(yLeader || hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.LEADER));
    }

    private void positionUpdate(PositionComponent positionComponent) {
        positionComponent.position = positionComponent.getPotentialPosition();
    }

    public void moveToTileEdgeX(PositionComponent positionComponent) {
        if (positionComponent.sign.x > 0) {
            positionComponent.position.x = ((int) (positionComponent.position.x / Map.getTileWidth()) + 1) * Map.getTileWidth()
                    - positionComponent.gameObject.getComponent(ColliderComponent.class).getRectWidth() - 1;
        }
        if (positionComponent.sign.x < 0) {
            positionComponent.position.x = (int) (positionComponent.position.x / Map.getTileWidth()) * Map.getTileWidth();
        }
    }

    public void moveToTileEdgeY(PositionComponent positionComponent) {
        if (positionComponent.sign.y < 0) {
            positionComponent.position.y = (int) (positionComponent.position.y / Map.getTileHeight()) * Map.getTileHeight();
        } else {
            int height = positionComponent.gameObject.getComponent(ColliderComponent.class).getRectHeight();
            positionComponent.position.y = ((int) ((positionComponent.position.y + height) / Map.getTileHeight()) + 1)
                    * Map.getTileHeight() - height - 1;
        }
    }

    private boolean hasCollisionOfType(ColliderType[] collisionCornersFlags, ColliderType type) {
        for (ColliderType flagType : collisionCornersFlags)
            if (flagType == type)
                return true;
        return false;
    }

    private void keysMovementManager(KeyboardControllerComponent component) {
        GameObject gameObject = component.gameObject;
        PositionComponent positionComponent = gameObject.getComponent(PositionComponent.class);

        if (component.isSpaceKeyPressed())
            if (gameObject.hasComponent(DyncamicColliderComponent.class))
                if (gameObject.getComponent(DyncamicColliderComponent.class).isOnGround()) {
                    positionComponent.sign.y = -1;
                    positionComponent.velocity.y = 0;
                    //animationStateMachine.trigger("startJump");
                }
        if (component.isWKeyPressed())
            if (gameObject.hasComponent(DyncamicColliderComponent.class))
                if (gameObject.getComponent(DyncamicColliderComponent.class).isOnLeader()) {
                    if (positionComponent.sign.y > 0)
                        positionComponent.velocity.y = 0;
                    positionComponent.sign.y = -1;
                    gameObject.getComponent(AnimationStateMachine.class).trigger("startClimb");
                    //TODO: Vezi can PostionComponent la posVelocity incurca urcatul pe scara cu saritul
                    //TODO: verifica doar pe colturile de sus daca are coliziune cu scara
                    //TODO: desparte cumva ce face playerul ce activitate ca sa nu mai fie problema intre sarit urcat si chestii
                }
    }
}
