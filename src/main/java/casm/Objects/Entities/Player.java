package casm.Objects.Entities;

import casm.ECS.Components.*;
import casm.ECS.Components.Collision.ColliderType;
import casm.Factory.EntityFactory.EntityType;
import casm.Game;
import casm.Objects.InfoBar;
import casm.StateMachine.AnimationStateMachine.AnimationState;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Camera;
import casm.Utils.Settings.EntitiesSettings;
import casm.Utils.Vector2D;

import java.util.List;
import java.util.Objects;

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
        this.getComponent(PositionComponent.class).setMaxSpeed(EntitiesSettings.PlayerInfo.PLAYER_MAX_SPEED);
        this.addComponent(new AttackComponent(EntityType.PLAYER));
        this.getComponent(AttackComponent.class).setAttackDelay(4L);
        this.addComponent(new KeyboardControllerComponent());
        this.addComponent(Camera.getInstance());
        this.setDamage(15);
        //this.init();
    }

    private void generateAnimationStateMachine() {
        AnimationStateMachine stateMachine = new AnimationStateMachine();
        List<AnimationState> animationStates = AnimationsExtract.getInstance().extractAnimations("player_animation.json");

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
        stateMachine.addState("attack", "idle", "stopAttack_idle");
        stateMachine.addState("attack", "run", "stopAttack_run");

        stateMachine.addState("idle", "dead", "Dead");
        stateMachine.addState("run", "dead", "Dead");

        this.addComponent(stateMachine);
    }

    @Override
    public void setLife(double life) {
        super.setLife(life);
        Objects.requireNonNull(Game.getLevelScene()).infoBar.updateHealth(this.getLife());
    }

    @Override
    public void damage(double damage) {
        super.damage(damage);
        Objects.requireNonNull(Game.getLevelScene()).infoBar.updateHealth(this.getLife());
    }
    @Override
    public void revive(double health) {
        super.revive(health);
        Objects.requireNonNull(Game.getLevelScene()).infoBar.updateHealth(this.getLife());
    }
}
