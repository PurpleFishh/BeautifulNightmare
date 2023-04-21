package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.*;
import casm.ECS.GameObject;
import casm.Game;
import casm.Map.Map;
import casm.Utils.Mediator;
import casm.Utils.Vector2D;

public class MovementMediator implements Mediator {

    //TODO: Mediatorul SingleTone inca nu merge cu mi-as dori, mai exact el ar fi trebuit sa fie cate un mediator pentru fiecare GameObject, nu sa se intializeze cate unul pentru fiecare Componenta a unui obiect
    private static GameObject gameObject = null;
    private static MovementMediator instance = null;

    public static MovementMediator getInstance(GameObject gameObject) {
        if (MovementMediator.gameObject == null)
            instance = new MovementMediator(gameObject);
        return instance;
    }

    private MovementMediator(GameObject gameObject) {
        MovementMediator.gameObject = gameObject;
    }

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
        Rectangle playerCollider = gameObject.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY);

        Vector2D potentialPosition = positionComponent.getPotentialPosition();
        Vector2D potentialPositionColl = positionComponent.getPotentialPosition(playerCollider.getPosition());

        Vector2D xPlayerPotential = new Vector2D(potentialPosition.x, positionComponent.position.y);
        Vector2D yPlayerPotential = new Vector2D(positionComponent.position.x, potentialPosition.y);
        Vector2D xColliderPotential = new Vector2D(potentialPositionColl.x, playerCollider.getY());
        Vector2D yColliderPotential = new Vector2D(playerCollider.getX(), potentialPositionColl.y);


        dynamicColl.checkCollision(xColliderPotential, (int) playerCollider.getWidth(), (int) playerCollider.getHeight(),
                Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
        boolean collisionX = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.MAP_TILE);
        if (collisionX) {
            moveToTileEdgeX(positionComponent, playerCollider);
        } else
            positionComponent.position.x = xPlayerPotential.x;

        boolean xLeaderMiddle = dynamicColl.getCollisionCorrnersFlags()[4] == ColliderType.LEADER || dynamicColl.getCollisionCorrnersFlags()[5] == ColliderType.LEADER;
        boolean xLeaderBottom = dynamicColl.getCollisionCorrnersFlags()[2] == ColliderType.LEADER || dynamicColl.getCollisionCorrnersFlags()[3] == ColliderType.LEADER;

        /// Check for Y movement
        dynamicColl.checkCollision(yColliderPotential, (int) playerCollider.getWidth(), (int) playerCollider.getHeight(),
                Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());

        boolean collisionY = hasCollisionOfType(dynamicColl.getCollisionCorrnersFlags(), ColliderType.MAP_TILE);
        boolean yLeaderMiddle = dynamicColl.getCollisionCorrnersFlags()[4] == ColliderType.LEADER || dynamicColl.getCollisionCorrnersFlags()[5] == ColliderType.LEADER;
        boolean yLeaderBottom = dynamicColl.getCollisionCorrnersFlags()[2] == ColliderType.LEADER || dynamicColl.getCollisionCorrnersFlags()[3] == ColliderType.LEADER;
        if (collisionY)
            moveToTileEdgeY(positionComponent, playerCollider);
        else {
            if (positionComponent.isClimbing() && !(xLeaderBottom || yLeaderBottom)) {
                positionComponent.sign.y = 0;
                positionComponent.velocity.y = 0;
                positionComponent.setClimbing(false);
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopClimb");
            } else if (!(positionComponent.isClimbing() && !(yLeaderMiddle || xLeaderMiddle) && (xLeaderBottom || yLeaderBottom)))
                positionComponent.position.y = yPlayerPotential.y;
        }
        dynamicColl.setOnGround(dynamicColl.getCollisionCorrnersFlags()[2] == ColliderType.MAP_TILE || dynamicColl.getCollisionCorrnersFlags()[3] == ColliderType.MAP_TILE);
        dynamicColl.setOnLeader(yLeaderMiddle || xLeaderMiddle);
    }

    private void positionUpdate(PositionComponent positionComponent) {
        positionComponent.position = positionComponent.getPotentialPosition();
    }

    public void moveToTileEdgeX(PositionComponent positionComponent, Rectangle collider) {
        if (positionComponent.sign.x > 0) {
            double oldPosition = positionComponent.position.x;
            positionComponent.position.x = ((int) (collider.getX() / Map.getTileWidth()) + 1) * Map.getTileWidth()
                    - collider.getWidth() - collider.getOffset().x - 1 + oldPosition;
            positionComponent.position.x += oldPosition - positionComponent.position.x;
            //TODO: Trebuie reparat BugFix-ul asta: il folosim deoare ce AiBehaviour vedem daca avem collziune pe o parte si daca da mergem in cealata parte, dar asa noi il lipim fix langa tile fara coliziune(ceea ce vrem pentru player)
            if(positionComponent.gameObject.getName().equals("enemy")) {
                positionComponent.position.x += 1;
            }
        }
        if (positionComponent.sign.x < 0) {
            double oldPosition = positionComponent.position.x;
            positionComponent.position.x = (int) (collider.getX() / Map.getTileWidth()) * Map.getTileWidth() - collider.getOffset().x;
            positionComponent.position.x += oldPosition - positionComponent.position.x;
            if(positionComponent.gameObject.getName().equals("enemy"))
                positionComponent.position.x -= 1;
        }
    }

    public void moveToTileEdgeY(PositionComponent positionComponent, Rectangle collider) {
        if (positionComponent.sign.y < 0) {
            positionComponent.position.y = (int) (collider.getY() / Map.getTileHeight()) * Map.getTileHeight() + 1 - collider.getOffset().y;

            positionComponent.sign.y = 0;
            positionComponent.velocity.y = 0;
        } else {
            positionComponent.position.y = ((int) ((collider.getY() + collider.getHeight()) / Map.getTileHeight()) + 1)
                    * Map.getTileHeight() - collider.getHeight() - 1 - collider.getOffset().y;
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
                    positionComponent.setJumping(true);
                }
        if (component.getWKeyState() == KeyState.PRESSED) {
            if (gameObject.hasComponent(DyncamicColliderComponent.class))
                if (gameObject.getComponent(DyncamicColliderComponent.class).isOnLeader()) {
                    if (!positionComponent.isClimbing()) {
                        positionComponent.velocity.y = 0;
                        positionComponent.sign.y = -1;
                        positionComponent.setClimbing(true);
                        gameObject.getComponent(AnimationStateMachine.class).trigger("startClimb");
                        component.resetWKeyState();
                    }
                }
        }
        if (component.getWKeyState() == KeyState.RELEASED) {
            if (positionComponent.isClimbing()) {
                positionComponent.sign.y = 0;
                positionComponent.velocity.y = 0;
                positionComponent.setClimbing(false);
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopClimb");
                component.resetWKeyState();
            }
        }
        if (component.getFKeyState() == KeyState.PRESSED) {
            if (gameObject.hasComponent(AttackComponent.class)) {
                gameObject.getComponent(AttackComponent.class).attack();
            }
        }
//        if (component.getFKeyState() == KeyState.RELEASED) {
//            if (gameObject.hasComponent(AttackComponent.class)) {
//                gameObject.getComponent(AnimationStateMachine.class).trigger("stopAttack");
//                component.resetFKeyState();
//            }
//        }

    }
}
