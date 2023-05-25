package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.Components.Collision.Rectangle;
import casm.Game;
import casm.Scenes.Level.LeveleScene;
import casm.StateMachine.AiStateMachine.GameStateMachine;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.FlipEntityMediator;

/**
 * Used for entities AI for movement, following target and attacking
 */
public class AiBehaviour extends Component {

    //TODO: facuta in graba, Inamicii vor avea Ai facut cum trebuie, sa urmareasca jucatorul(cand este aproape) si sa il atace

    /**
     * Entity position component
     * @see PositionComponent
     */
    private PositionComponent positionComponent;
    /**
     * Entity dynamic collider component
     * @see DyncamicColliderComponent
     */
    private DyncamicColliderComponent dyncamicColliderComponent;
    /**
     * aiDetection - if the chase entity is in that collider, it will follow it<br>
     * chase - the collider of the chase entity<br>
     * gameObjectHitBox - the hit box of the entity, where it can attack
     */
    private Rectangle aiDetection, chase, gameObjectHitBox;
    /**
     * The entity animation state machine
     */
    private GameStateMachine stateMachine;

    /**
     * Getting the references to the components
     */
    @Override
    public void init() {
        positionComponent = gameObject.getComponent(PositionComponent.class);
        dyncamicColliderComponent = gameObject.getComponent(DyncamicColliderComponent.class);
        gameObjectHitBox = gameObject.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY);
        aiDetection = gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.AI_DETECTION, (int) -(200 / 2 - gameObjectHitBox.getHeight() / 2), 0, 200, (int) gameObjectHitBox.height);
        chase = ((LeveleScene) Game.getCurrentScene()).getPlayer().getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY);

        positionComponent.sign.x = -1;
    }

    /**
     * If the chase entity is in the view area(aiDetection), it will follow it<br>
     * If the entity can attack the enemy it will do<br>
     * If no of the scenarios is archived it will patrol from left to right
     */
    @Override
    public void update() {
        boolean inRange = aiDetection.intersects(chase);
        if (inRange) {
            //TODO: Sa se pozitioneze incat sa fie in area de hit nu sa il urmareasca pana in centru
            // Daca e in coliziune cu hit boxu enemy sa mearga in partea in care e indreptat ca sa poata lovi jucatorul
            Rectangle leftRect = new Rectangle(aiDetection.x, aiDetection.y, aiDetection.getWidth() / 2 - gameObjectHitBox.width / 2, aiDetection.getHeight());
            Rectangle rightRect = new Rectangle(aiDetection.getCenterX() + gameObjectHitBox.width / 2, aiDetection.y, aiDetection.getWidth() / 2, aiDetection.getHeight());
            if (gameObject.getComponent(AttackComponent.class).getAttackCollider().intersects(chase)) {
                if (!gameObject.getComponent(AttackComponent.class).isAttacking()) {
                    gameObject.getComponent(AttackComponent.class).attack();
                    positionComponent.sign.x = 0;
                    positionComponent.velocity.x = 0;
                }
            }

            if (leftRect.intersects(chase)) {
                positionComponent.sign.x = -1;
                FlipEntityMediator.getInstance().flipHorizontally(gameObject, true);
                gameObject.getComponent(AnimationStateMachine.class).trigger("startRun");
            } else if (rightRect.intersects(chase)) {
                positionComponent.sign.x = 1;
                FlipEntityMediator.getInstance().flipHorizontally(gameObject, false);
                gameObject.getComponent(AnimationStateMachine.class).trigger("startRun");
            } else {
                positionComponent.sign.x = 0;
                positionComponent.velocity.x = 0;
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopRun");
            }

        } else {
            if (positionComponent.sign.x == 0) {
                positionComponent.sign.x = 1;
                FlipEntityMediator.getInstance().flipHorizontally(gameObject, false);
            }
            gameObject.getComponent(AnimationStateMachine.class).trigger("startRun");
        }

        ColliderType[] collisionFlags = dyncamicColliderComponent.getCollisionCornersFlags();
        if (collisionFlags[0] == ColliderType.MAP_TILE || collisionFlags[4] == ColliderType.MAP_TILE || collisionFlags[2] != ColliderType.MAP_TILE) {
            if (inRange) {
                positionComponent.sign.x = 0;
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopRun");
            }
            else {
                positionComponent.sign.x = 1;
                FlipEntityMediator.getInstance().flipHorizontally(gameObject, false);
            }
            positionComponent.velocity.x = 0;
        }
        if (collisionFlags[1] == ColliderType.MAP_TILE || collisionFlags[5] == ColliderType.MAP_TILE || collisionFlags[3] != ColliderType.MAP_TILE) {
            if (inRange) {
                positionComponent.sign.x = 0;
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopRun");
            }
            else {
                positionComponent.sign.x = -1;
                FlipEntityMediator.getInstance().flipHorizontally(gameObject, true);
            }
            positionComponent.velocity.x = 0;
        }
    }
}
