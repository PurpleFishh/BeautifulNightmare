package casm.Objects.Entities;

import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;
import casm.Objects.Object;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.StateMachine.AnimationStateMachine.AnimationState;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Vector2D;

import java.util.List;

public class WinDoor extends Object {
    private Vector2D size;
    /**
     * Create a new entity
     *
     * @param name name for the entity
     */
    public WinDoor(String name) {
        super(name, new Vector2D(0, 0));
        initialize();
    }

    public WinDoor(String name, Vector2D position) {
        super(name, position);
        initialize();
    }

    public void initialize() {
        generateAnimationStateMachine();
        super.updateDimensions((int) size.x, (int) size.y);
        this.addComponent(new SpriteComponent());
        this.addComponent(new ColliderComponent(ColliderType.WIN_DOOR, (int) size.x, (int) size.y));

        //this.init();
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
