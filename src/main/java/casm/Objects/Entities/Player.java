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

/**
 * The player of the game
 */
public class Player extends Entity {

    /**
     * <b>playerWidth</b> - Width of the player<br>
     * <b>playerHeight</b> - Height of the player
     */
    private int playerWidth, playerHeight;

    /**
     * @param spawnPosition Position where the player will be spawned
     */
    public Player(Vector2D spawnPosition) {
        super("player", spawnPosition, 0, 0, ColliderType.ENTITY, 0, 0);
        playerInit();
    }

    /**
     * @param name          Name of the player
     * @param spawnPosition Position where the player will be spawned
     */
    public Player(String name, Vector2D spawnPosition) {
        super(name, spawnPosition, 0, 0, ColliderType.ENTITY, 0, 0);
        playerInit();

    }

    /**
     * Initializes the player
     */
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

    /**
     * Generates the animation state machine of the player<br>
     * States: <b>idle, run, jump, climb, attack, dead</b><br>
     * <b>Transitions:</b><br>
     * <b>idle -> run</b>: startRun<br>
     * <b>run -> idle</b>: stopRun<br>
     * <b>idle -> jump</b>: startJump<br>
     * <b>run -> jump</b>: startJump<br>
     * <b>jump -> idle</b>: stopJump_StartIdle<br>
     * <b>jump -> run</b>: stopJump_StartRun<br>
     * <b>run -> climb</b>: startClimb<br>
     * <b>idle -> climb</b>: startClimb<br>
     * <b>climb -> idle</b>: stopClimb<br>
     * <b>run -> attack</b>: startAttack<br>
     * <b>idle -> attack</b>: startAttack<br>
     * <b>attack -> idle</b>: stopAttack_idle<br>
     * <b>attack -> run</b>: stopAttack_run<br>
     * <b>run -> dead</b>: Dead<br>
     * <b>idle -> dead</b>: Dead<br>
     */
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

    /**
     * Update player health
     * @param life Life of the player
     */
    @Override
    public void setLife(double life) {
        super.setLife(life);
        Objects.requireNonNull(Game.getLevelScene()).infoBar.updateHealth(this.getLife());
    }

    /**
     * Applies damage to the player
     * @param damage Damage to be applied to the player
     */
    @Override
    public void damage(double damage) {
        super.damage(damage);
        Objects.requireNonNull(Game.getLevelScene()).infoBar.updateHealth(this.getLife());
    }

    /**
     * Revives the player
     * @param health Health to be added to the player
     */
    @Override
    public void revive(double health) {
        super.revive(health);
        Objects.requireNonNull(Game.getLevelScene()).infoBar.updateHealth(this.getLife());
    }
}
