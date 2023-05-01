package casm.Entities;

import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.StateMachine.AnimationStateMachine.AnimationState;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Vector2D;

import java.util.List;

public class WinDoor extends GameObject {
    private Vector2D size;
    /**
     * Create a new entity
     *
     * @param name name for the entity
     */
    public WinDoor(String name) {
        super(name);
        initialize(0, 0);
    }

    public WinDoor(String name, Vector2D position) {
        super(name);
        initialize((int) position.x, (int) position.y);
    }

    public void initialize(int x, int y) {
        generateAnimationStateMachine();
        this.addComponent(new PositionComponent(x, y, (int) size.x, (int) size.y));
        this.addComponent(new SpriteComponent());
        this.addComponent(new ColliderComponent(ColliderType.WIN_DOOR, (int) size.x, (int) size.y));

        this.init();
    }
    private void generateAnimationStateMachine() {
        AnimationStateMachine stateMachine = new AnimationStateMachine();
        List<AnimationState> animationStates = AnimationsExtract.getInstance().extractAnimations("new_level_door_animation.json");

        animationStates.forEach(stateMachine::addState);
        stateMachine.setDefaultState(animationStates.get(0).getName());
        size = new Vector2D(animationStates.get(0).getCurrentSprite().getWidth(),
                animationStates.get(0).getCurrentSprite().getHeight());

        stateMachine.addState("closed", "opened", "open");

        this.addComponent(stateMachine);
    }
}
