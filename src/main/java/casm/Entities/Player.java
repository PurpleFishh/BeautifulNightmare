package casm.Entities;

import casm.ECS.Components.*;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.GameObject;
import casm.SpriteUtils.Animation.AnimationState;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.Utils.Camera;

import java.util.List;

public class Player extends GameObject {

    private int playerWidth, playerHeight;
    private final int playerSpawnPosX = 200, playerSpawnPosy = 30;

    public Player() {
        super("player");
        playerInit();
    }
    public Player(String name) {
        super(name);
        playerInit();
    }

    private void playerInit()
    {
        generateAnimationStateMachine();
        //Sprite playerSprite = AssetsCollection.getSpritesheet("player_single_frame.png").getSprite(0);
        //Sprite playerSprite = AnimationsExtract.animationsList.get(1).getSprite(3);
        this.addComponent(new PositionComponent(playerSpawnPosX, playerSpawnPosy, playerWidth, playerHeight, 1, 1, true));
        this.addComponent(new SpriteComponent());
        this.addComponent(new KeyboardControllerComponent());
        this.addComponent(new ColliderComponent(ColliderType.PLAYER,playerWidth - 2, playerHeight - 2));
        this.addComponent(new DyncamicColliderComponent());
        this.addComponent(Camera.getInstance());

        this.init();
    }

    private void generateAnimationStateMachine()
    {
        AnimationStateMachine stateMachine = new AnimationStateMachine();
        List<AnimationState> animationStates =  AnimationsExtract.extractAnimations("player_animation.json");

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
        this.addComponent(stateMachine);
    }

}
