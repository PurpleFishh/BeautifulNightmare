package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.Components.Collision.Rectangle;
import casm.Game;
import casm.Scenes.LeveleScene;
import casm.StateMachine.AiStateMachine.AiIdleState;
import casm.StateMachine.AiStateMachine.AiStateMachine;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.FlipEntityMediator;

public class AiBehaviour extends Component {

    //TODO: facuta in graba, Inamicii vor avea Ai facut cum trebuie, sa urmareasca jucatorul(cand este aproape) si sa il atace

    private PositionComponent positionComponent;
    private DyncamicColliderComponent dyncamicColliderComponent;
    private Rectangle aiDetection, chase, gameObjectHitBox;
    private AiStateMachine stateMachine;

    @Override
    public void init() {
        positionComponent = gameObject.getComponent(PositionComponent.class);
        dyncamicColliderComponent = gameObject.getComponent(DyncamicColliderComponent.class);
        gameObjectHitBox = gameObject.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY);
        aiDetection = gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.AI_DETECTION, (int) -(200 / 2 - gameObjectHitBox.getHeight() / 2), 0, 200, (int) gameObjectHitBox.height);
        chase = ((LeveleScene) Game.getCurrentScene()).getPlayer().getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY);

        stateMachine = new AiStateMachine(gameObject);
        stateMachine.addState(new AiIdleState(gameObject, "Idle"));

        positionComponent.sign.x = -1;
    }

    @Override
    public void update() {
        if (aiDetection.intersects(chase)) {
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
                FlipEntityMediator.getInstance().flipVertically(gameObject, true);
                gameObject.getComponent(AnimationStateMachine.class).trigger("startRun");
            } else if (rightRect.intersects(chase)) {
                positionComponent.sign.x = 1;
                FlipEntityMediator.getInstance().flipVertically(gameObject, false);
                gameObject.getComponent(AnimationStateMachine.class).trigger("startRun");
            } else {
                positionComponent.sign.x = 0;
                positionComponent.velocity.x = 0;
                gameObject.getComponent(AnimationStateMachine.class).trigger("stopRun");
            }

        } else {
            gameObject.getComponent(AnimationStateMachine.class).trigger("startRun");
        }

        ColliderType[] collisionFlags = dyncamicColliderComponent.getCollisionCornersFlags();
        if (collisionFlags[0] == ColliderType.MAP_TILE || collisionFlags[4] == ColliderType.MAP_TILE || collisionFlags[2] != ColliderType.MAP_TILE) {
            positionComponent.sign.x = 1;
            positionComponent.velocity.x = 0;
            FlipEntityMediator.getInstance().flipVertically(gameObject, false);
        }
        if (collisionFlags[1] == ColliderType.MAP_TILE || collisionFlags[5] == ColliderType.MAP_TILE || collisionFlags[3] != ColliderType.MAP_TILE) {
            positionComponent.sign.x = -1;
            positionComponent.velocity.x = 0;
            FlipEntityMediator.getInstance().flipVertically(gameObject, true);
        }
    }
}
