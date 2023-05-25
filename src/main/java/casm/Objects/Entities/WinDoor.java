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

/**
 * The door that leads to the next level<br>
 * It opens when the player completes all the tasks
 */
public class WinDoor extends Object {
    /**
     * Size of the door
     */
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

    /**
     * @param name    name for the entity
     * @param position position for the entity
     */
    public WinDoor(String name, Vector2D position) {
        super(name, position);
        initialize();
    }

    /**
     * Initialize the entity
     */
    public void initialize() {
        generateAnimationStateMachine();
        super.updateDimensions((int) size.x, (int) size.y);
        this.addComponent(new SpriteComponent());
        this.addComponent(new ColliderComponent(ColliderType.WIN_DOOR, (int) size.x, (int) size.y));

        //this.init();
    }

    /**
     * Generate the animation state machine for the door
     * It has 2 states: closed and opened
     * To open the door, the state machine trigger is "open" from closed to opened
     */
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
