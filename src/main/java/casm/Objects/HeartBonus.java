package casm.Objects;

import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.Rectangle;
import casm.ECS.Components.SpriteComponent;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.StateMachine.AnimationStateMachine.AnimationState;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Vector2D;

import java.util.List;

/**
 * Object that represents a heart bonus for recovering life
 */
public class HeartBonus extends Object {

    /**
     * Collider of the heart bonus for collision detection
     */
    private Rectangle collider;

    /**
     * @param spawnPosition Position where the heart bonus will be spawned
     */
    public HeartBonus(Vector2D spawnPosition) {
        super("Heart", spawnPosition);
        entityInit();
    }

    /**
     * Initializes the heart bonus
     */
    private void entityInit() {
        this.addComponent(new SpriteComponent());
        AnimationStateMachine stateMachine = new AnimationStateMachine();
        List<AnimationState> animationStates = AnimationsExtract.getInstance().extractAnimations("heart_animation.json");

        animationStates.forEach(stateMachine::addState);
        stateMachine.setDefaultState(animationStates.get(0).getName());

        int width = animationStates.get(0).getCurrentSprite().getWidth();
        int height = animationStates.get(0).getCurrentSprite().getHeight();
        this.updateDimensions(width, height);

        this.addComponent(stateMachine);
        this.addComponent(new ColliderComponent(ColliderType.HEART_BONUS, width / 2 - 2, 0, 5, height));
        collider = this.getComponent(ColliderComponent.class).getCollider(ColliderType.HEART_BONUS);

        //this.init();
    }

    /**
     * @return Collider of the heart bonus for collision detection
     */
    public Rectangle getCollider() {
        return collider;
    }
}

