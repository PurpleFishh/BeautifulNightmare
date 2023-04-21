package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.Components.Collision.Rectangle;
import casm.Game;
import casm.Map.Map;

public class AiBehaviour extends Component {

    //TODO: facuta in graba, Inamicii vor avea Ai facut cum trebuie, sa urmareasca jucatorul(cand este aproape) si sa il atace

    PositionComponent positionComponent;
    DyncamicColliderComponent dyncamicColliderComponent;

    @Override
    public void init() {
        positionComponent = gameObject.getComponent(PositionComponent.class);
        positionComponent.sign.x = 1;
        dyncamicColliderComponent = gameObject.getComponent(DyncamicColliderComponent.class);
    }

    @Override
    public void update() {
        gameObject.getComponent(AnimationStateMachine.class).trigger("startRun");
        ColliderType[] collisionFlags = dyncamicColliderComponent.getCollisionCorrnersFlags();
        if (collisionFlags[0] == ColliderType.MAP_TILE || collisionFlags[4] == ColliderType.MAP_TILE || collisionFlags[2] == null) {
            positionComponent.sign.x = 1;
            positionComponent.velocity.x = 0;
            gameObject.getComponent(AttackComponent.class).setFlipColliderHorizontally(false);
            gameObject.getComponent(SpriteComponent.class).setFlippedVertically(false);
        }
        if (collisionFlags[1] == ColliderType.MAP_TILE || collisionFlags[5] == ColliderType.MAP_TILE || collisionFlags[3] == null) {
            positionComponent.sign.x = -1;
            positionComponent.velocity.x = 0;
            gameObject.getComponent(AttackComponent.class).setFlipColliderHorizontally(true);
            gameObject.getComponent(SpriteComponent.class).setFlippedVertically(true);
        }
    }
}
