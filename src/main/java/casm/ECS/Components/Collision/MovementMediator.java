package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.AnimationStateMachine;
import casm.ECS.Components.KeyState;
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
        if (component instanceof KeyboardControllerComponent)
            keysMovementManager((KeyboardControllerComponent) component);
        if (component instanceof PositionComponent) {
            if (component.gameObject.hasComponent(DyncamicColliderComponent.class))
                collisionManager((PositionComponent) component);
            else
                positionUpdate((PositionComponent) component);
        }
    }

    private void collisionManager(PositionComponent positionComponent) {
        GameObject gameObject = positionComponent.gameObject;
        DyncamicColliderComponent dynamicColl = gameObject.getComponent(DyncamicColliderComponent.class);
        Vector2D potentialPosition = positionComponent.getPotentialPosition();

        Vector2D xPot = new Vector2D(potentialPosition.x, positionComponent.position.y);
        Vector2D yPot = new Vector2D(positionComponent.position.x, potentialPosition.y);

        int width = gameObject.getComponent(ColliderComponent.class).getRectWidth();
        int height = gameObject.getComponent(ColliderComponent.class).getRectHeight();

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
        boolean xLeaderMiddle = dynamicColl.getCollisionCorrnersFlags()[4] == ColliderType.LEADER || dynamicColl.getCollisionCorrnersFlags()[5] == ColliderType.LEADER;
        boolean xLeaderBottom = dynamicColl.getCollisionCorrnersFlags()[2] == ColliderType.LEADER || dynamicColl.getCollisionCorrnersFlags()[3] == ColliderType.LEADER;

        /// Check for Y movement
        dynamicColl.checkCollision(yPot, width, height,
                Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());

        boolean collisionY = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.MAP_TILE);
        boolean yLeaderMiddle = dynamicColl.getCollisionCorrnersFlags()[4] == ColliderType.LEADER || dynamicColl.getCollisionCorrnersFlags()[5] == ColliderType.LEADER;
        boolean yLeaderBottom = dynamicColl.getCollisionCorrnersFlags()[2] == ColliderType.LEADER || dynamicColl.getCollisionCorrnersFlags()[3] == ColliderType.LEADER;
        if (collisionY)
            moveToTileEdgeY(positionComponent);
        else {
            if (positionComponent.isClimbing() && !(xLeaderBottom || yLeaderBottom))
            {
                positionComponent.sign.y = 0;
                positionComponent.velocity.y = 0;
                positionComponent.setClimbing(false);
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopClimb");
            } else if (!(positionComponent.isClimbing() && !(yLeaderMiddle || xLeaderMiddle) && (xLeaderBottom || yLeaderBottom))) {
                positionComponent.position.y = yPot.y;
                //moveToTileEdgeY(positionComponent);
                //positionComponent.sign.y = 0;
               // positionComponent.velocity.y = 0;
                //positionComponent.setClimbing(false);
                //gameObject.getComponent(AnimationStateMachine.class).trigger("stopClimb");
            }
        }
        dynamicColl.setOnLeader(yLeaderMiddle || xLeaderMiddle);
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
            positionComponent.position.y = (int) (positionComponent.position.y / Map.getTileHeight()) * Map.getTileHeight() + 1;
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

        if (component.getSpaceKeyState() == KeyState.PRESSED)
            if (gameObject.hasComponent(DyncamicColliderComponent.class))
                if (gameObject.getComponent(DyncamicColliderComponent.class).isOnGround()) {
                    positionComponent.sign.y = -1;
                    positionComponent.velocity.y = 0;
                    //animationStateMachine.trigger("startJump");
                    positionComponent.setJumping(true);
                }
        if (component.getWKeyState() == KeyState.PRESSED) {
            if (gameObject.hasComponent(DyncamicColliderComponent.class))
                if (gameObject.getComponent(DyncamicColliderComponent.class).isOnLeader()) {
                    //if (positionComponent.sign.y > 0)
                    positionComponent.velocity.y = 0;
                    positionComponent.sign.y = -1;
                    positionComponent.setClimbing(true);
                    gameObject.getComponent(AnimationStateMachine.class).trigger("startClimb");
                    //TODO: Problema cand se urca pe scara viteza nu e constanta, se diminueaza deodta...
                }
        }
        if (component.getWKeyState() == KeyState.RELEASED) {
            if(positionComponent.isClimbing()) {
                System.out.println("A jauns");
                positionComponent.sign.y = 0;
                positionComponent.velocity.y = 0;
                positionComponent.setClimbing(false);
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopClimb");
                component.resetWKeyState();
            }
        }

    }
}
