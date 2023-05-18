package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.*;
import casm.ECS.GameObject;
import casm.Objects.Entities.Entity;
import casm.Game;
import casm.Map.Map;
import casm.Objects.Entities.Player;
import casm.Objects.HeartBonus;
import casm.Scenes.Level.LevelSaverLoader;
import casm.Scenes.Level.LeveleScene;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.FlipEntityMediator;
import casm.Utils.Mediator;
import casm.Utils.Settings.EntitiesSettings;
import casm.Utils.Settings.Setting;
import casm.Utils.Vector2D;

import java.sql.SQLException;
import java.util.Objects;

/**
 * The mediator that is used for every entity for movement<br>
 * It resolve the dependencies between components finding solutions for problems like entity movement,
 * collision resolver, keyboard movement
 */
public class MovementMediator implements Mediator {

    //TODO: Mediatorul SingleTone inca nu merge cu mi-as dori, mai exact el ar fi trebuit sa fie cate un mediator pentru fiecare GameObject, nu sa se intializeze cate unul pentru fiecare Componenta a unui obiect
    /**
     * The object that the mediator is resolving movement for<br>
     * The class is singleton for every object(this is the reference to that object)
     */
    private static GameObject gameObject = null;
    /**
     * The instance of the class used in the singleton
     */
    private static MovementMediator instance = null;
    /**
     * Time delay used for damaging by lava, because we don't want the lava to damage on every update, so a delay is a good solution
     */
    private double lavaDamageDelay = 0;

    /**
     * Get the instance of the singleton
     *
     * @param gameObject the object that the class will be singleton to
     * @return the instance of the class
     */
    public static MovementMediator getInstance(GameObject gameObject) {
        if (MovementMediator.gameObject == null)
            instance = new MovementMediator(gameObject);
        return instance;
    }

    private MovementMediator(GameObject gameObject) {
        MovementMediator.gameObject = gameObject;
    }

    /**
     * Used when something changes in the component, to notify the mediator, and it will deal with that if it has support for that problem
     *
     * @param component the component from which you notify the component
     */
    @Override
    public void notify(Component component) {
        if (component instanceof KeyboardControllerComponent)
            keysMovementManager((KeyboardControllerComponent) component);
        if (component instanceof PositionComponent) {
            if (component.gameObject.hasComponent(DyncamicColliderComponent.class)) {
                //TODO: Quick Fix: cand schimb scena la GameOver el mai da o data update la inamici si gaseste scena noua fara mapa
                if (Game.getCurrentScene().getGameObjects().contains(component.gameObject))
                    collisionManager((PositionComponent) component);
            } else
                positionUpdate((PositionComponent) component);
        }
    }

    /**
     * For resolving the collisions of an object, activate by {@link PositionComponent}<br>
     * This method is resolving the collisions with the map tiles, setting the flags for collision, if the entity is on leader,
     * if the entity is in collision with lava, and if it reached the final door to win the game<br>
     *
     * @param positionComponent the position component of the entity({@link PositionComponent})
     */
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

        boolean collisionX = hasCollisionOfType(dynamicColl.getCollisionCornersFlags(), ColliderType.MAP_TILE);
        if (collisionX) {
            moveToTileEdgeX(positionComponent, playerCollider);
        } else
            positionComponent.position.x = xPlayerPotential.x;

        boolean xLeaderMiddle = hasCollisionOfTypeOnMiddle(dynamicColl.getCollisionCornersFlags(), ColliderType.LEADER) ||
                hasCollisionOfTypeOnMiddle(dynamicColl.getCollisionCornersFlags(), ColliderType.LAVA);
        boolean xLeaderBottom = hasCollisionOfTypeOnBottom(dynamicColl.getCollisionCornersFlags(), ColliderType.LEADER) ||
                hasCollisionOfTypeOnBottom(dynamicColl.getCollisionCornersFlags(), ColliderType.LAVA);
        boolean xLavaCollision = hasCollisionOfTypeOnMiddle(dynamicColl.getCollisionCornersFlags(), ColliderType.LAVA);

        /// Check for Y movement
        dynamicColl.checkCollision(yColliderPotential, (int) playerCollider.getWidth(), (int) playerCollider.getHeight(),
                Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());

        boolean collisionY = hasCollisionOfType(dynamicColl.getCollisionCornersFlags(), ColliderType.MAP_TILE);

        boolean yLeaderMiddle = hasCollisionOfTypeOnMiddle(dynamicColl.getCollisionCornersFlags(), ColliderType.LEADER) ||
                hasCollisionOfTypeOnMiddle(dynamicColl.getCollisionCornersFlags(), ColliderType.LAVA);
        boolean yLeaderBottom = hasCollisionOfTypeOnBottom(dynamicColl.getCollisionCornersFlags(), ColliderType.LEADER) ||
                hasCollisionOfTypeOnBottom(dynamicColl.getCollisionCornersFlags(), ColliderType.LAVA);
        boolean yLavaCollision = hasCollisionOfTypeOnMiddle(dynamicColl.getCollisionCornersFlags(), ColliderType.LAVA);

        if (collisionY)
            moveToTileEdgeY(positionComponent, playerCollider);
        else {
            if(hasCollisionOfTypeOnBottom(dynamicColl.getCollisionCornersFlags(), ColliderType.VOID ))
                ((Entity)gameObject).setLife(0);
            if (positionComponent.isClimbing() && !(xLeaderBottom || yLeaderBottom)) {
                positionComponent.sign.y = 0;
                positionComponent.velocity.y = 0;
                positionComponent.setClimbing(false);
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopClimb");
            } else if (!(positionComponent.isClimbing() && !(yLeaderMiddle || xLeaderMiddle) && (xLeaderBottom || yLeaderBottom)))
                positionComponent.position.y = yPlayerPotential.y;
        }
        dynamicColl.setOnGround(dynamicColl.getCollisionCornersFlags()[2] == ColliderType.MAP_TILE || dynamicColl.getCollisionCornersFlags()[3] == ColliderType.MAP_TILE);

        dynamicColl.setOnLeader(yLeaderMiddle || xLeaderMiddle);

        //TODO: Fa interactiunea cu lava mai smechera
        if (lavaDamageDelay <= 0) {
            if (xLavaCollision || yLavaCollision)
                ((Entity) gameObject).damage(EntitiesSettings.LAVA_DAMAGE);
            lavaDamageDelay = 6;
        } else
            lavaDamageDelay -= 1 / Setting.DELTA_TIME;
        if (gameObject instanceof Player) {
            if (((Entity) gameObject).getLife() != 100)
                Objects.requireNonNull(Game.getLevelScene()).getHeartBonuses().forEach(heart -> {
                    if (heart.getCollider().intersects(playerCollider)) {
                        ((Player) gameObject).revive(40);
                        heart.kill();
                    }
                });
            verifyIfGameWon(playerCollider);
        }
    }

    /**
     * If the game was won, it verify if the player is in contact with the door that will send him to the next level
     *
     * @param playerCollider rectangle collider of the player
     */
    private void verifyIfGameWon(Rectangle playerCollider) {
        if (playerCollider.intersects(((LeveleScene) Game.getCurrentScene()).getWinDoor().
                getComponent(ColliderComponent.class).getCollider(ColliderType.WIN_DOOR)))
            if (((LeveleScene) Game.getCurrentScene()).isWon()) {
                System.out.println("Ai castigat");
                try {
                    LevelSaverLoader.getInstance().saveHighScore();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Game.changeLevel(((LeveleScene) Game.getCurrentScene()).getLevel() + 1, true);
            }
    }

    /**
     * If the entity is not in any collision, it will set its position to new potential position
     *
     * @param positionComponent entity position component({@link PositionComponent})
     */
    private void positionUpdate(PositionComponent positionComponent) {
        positionComponent.position = positionComponent.getPotentialPosition();
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
    private boolean hasCollisionOfTypeOnBottom(ColliderType[] collisionCornersFlags, ColliderType type) {
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
     * Used whene a key in use and it will manage what needs to be done after that action<br>
     * It is managing the entity movement(W, A, S, D), climbing leaders(Space) and attack(F)
     *
     * @param component the keyboard control component({@link KeyboardControllerComponent})
     */
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
        if (component.getAKeyState() == KeyState.PRESSED) {
            if (!component.right)
                positionComponent.sign.x = -1;
            component.left = true;
            //if(gameObject.getComponent(DyncamicColliderComponent.class).isOnGround())
            gameObject.getComponent(AnimationStateMachine.class).trigger("startRun");
            FlipEntityMediator.getInstance().flipVertically(gameObject, true);
        }
        if (component.getDKeyState() == KeyState.PRESSED) {
            if (!component.left) {
                positionComponent.sign.x = 1;
            }
            component.right = true;
            //if(gameObject.getComponent(DyncamicColliderComponent.class).isOnGround())
            gameObject.getComponent(AnimationStateMachine.class).trigger("startRun");
            FlipEntityMediator.getInstance().flipVertically(gameObject, false);
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
        if (component.getAKeyState() == KeyState.RELEASED) {
            if (component.right) {
                if (!(positionComponent.velocity.x > 0)) {
                    positionComponent.velocity.x *= -1;
                }
            } else {
                positionComponent.velocity.x = 0;
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopRun");
            }
            positionComponent.sign.x = 0;
            component.left = false;
            component.resetAKeyState();
        }
        if (component.getDKeyState() == KeyState.RELEASED) {
            if (component.left) {
                if (!(positionComponent.velocity.x < 0)) {
                    positionComponent.velocity.x *= -1;
                }
            } else {
                positionComponent.velocity.x = 0;
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopRun");
            }
            positionComponent.sign.x = 0;
            component.right = false;
            component.resetDKeyState();
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
