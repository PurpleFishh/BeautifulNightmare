package casm.Entities;

import casm.ECS.Components.*;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.GameObject;
import casm.SpriteUtils.Animation.AnimationState;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.Utils.Camera;
import casm.Utils.Vector2D;

import java.util.List;

public class Player extends Entity {

    private int playerWidth, playerHeight;

    public Player(Vector2D spawnPosition) {
        super("player", spawnPosition, 0, 0, ColliderType.ENTITY, 0, 0);
        playerInit();
    }

    public Player(String name, Vector2D spawnPosition) {
        super(name, spawnPosition, 0, 0, ColliderType.ENTITY, 0, 0);
        playerInit();

    }
    private void playerInit() {
        generateAnimationStateMachine();
        updateDimensions(playerWidth, playerHeight);
        this.addComponent(new KeyboardControllerComponent());
        this.addComponent(Camera.getInstance());
        this.setDamage(15);

        this.init();
    }

    private void generateAnimationStateMachine() {
        AnimationStateMachine stateMachine = new AnimationStateMachine();
        List<AnimationState> animationStates = AnimationsExtract.extractAnimations("player_animation.json");

        animationStates.forEach(stateMachine::addState);
        stateMachine.setDefaultState(animationStates.get(0).getName());
        playerWidth = animationStates.get(0).getCurrentSprite().getWidth();
        playerHeight = animationStates.get(0).getCurrentSprite().getHeight();

        stateMachine.addState("idle", "run", "startRun");
        stateMachine.addState("run", "idle", "stopRun");
        stateMachine.addState("idle", "jump", "startJump");
        stateMachine.addState("run", "jump", "startJump");
        stateMachine.addState("jump", "idle", "stopJump_StartIdle");
        stateMachine.addState("jump", "run", "stopJump_StartRun");

        stateMachine.addState("run", "climb", "startClimb");
        stateMachine.addState("idle", "climb", "startClimb");
        stateMachine.addState("climb", "idle", "stopClimb");

        stateMachine.addState("run", "attack", "startAttack");
        stateMachine.addState("idle", "attack", "startAttack");
        stateMachine.addState("attack", "idle", "stopAttack");
        this.addComponent(stateMachine);
    }

}
