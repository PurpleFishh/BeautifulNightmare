package casm.Objects.Entities.Enemies;

import casm.ECS.Components.AttackComponent;
import casm.ECS.Components.PositionComponent;
import casm.Factory.EntityFactory.EntityType;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.StateMachine.AnimationStateMachine.AnimationState;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Settings.EntitiesSettings;
import casm.Utils.Vector2D;

import java.util.List;

/**
 * Catfish is a type of enemy
 */
public class Catfish extends Enemy {
    /**
     * <b>enemyWidth</b> - Width of the enemy<br>
     * <b>enemyHeight</b> - Height of the enemy
     */
    private int enemyWidth, enemyHeight;

    /**
     * @param spawnPosition Position where the enemy will be spawned
     */
    public Catfish(Vector2D spawnPosition) {
        super(EntityType.CATFISH, spawnPosition, 0, 0);
        initiate();
    }

    /**
     * @param name          Name of the enemy
     * @param spawnPosition Position where the enemy will be spawned
     */
    public Catfish(String name, Vector2D spawnPosition) {
        super(name, EntityType.CATFISH, spawnPosition, 0, 0);
        initiate();
    }

    /**
     * Initializes the enemy
     */
    public void initiate() {
        generateAnimationStateMachine();
        this.getComponent(PositionComponent.class).setMaxSpeed(EntitiesSettings.CatfishInfo.CATFISH_MAX_SPEED);
        this.getComponent(AttackComponent.class).setAttackDelay(6L);
        updateDimensions(enemyWidth, enemyHeight);
        this.setDamage(16);
        this.setLife(40);


        // this.init();
    }

    /**
     * Generates the animation state machine<br>
     * States: <b>idle, run, attack, damage, dead</b><br>
     * <b>Transitions:</b><br>
     * <b>idle -> run</b>: startRun<br>
     * <b>run -> idle</b>: stopRun<br>
     * <b>idle -> attack</b>: startAttack<br>
     * <b>run -> attack</b>: startAttack<br>
     * <b>attack -> idle</b>: stopAttack_idle<br>
     * <b>attack -> run</b>: stopAttack_run<br>
     * <b>idle -> damage</b>: Damage<br>
     * <b>run -> damage</b>: Damage<br>
     * <b>damage -> idle</b>: endDamage<br>
     * <b>idle -> dead</b>: Dead<br>
     * <b>run -> dead</b>: Dead<br>
     * <b>attack -> dead</b>: Dead<br>
     * <b>damage -> dead</b>: Dead<br>
     */
    private void generateAnimationStateMachine() {
        AnimationStateMachine stateMachine = new AnimationStateMachine();
        List<AnimationState> animationStates = AnimationsExtract.getInstance().extractAnimations("enemy_catfish_animation.json");

        animationStates.forEach(stateMachine::addState);
        stateMachine.setDefaultState(animationStates.get(0).getName());
        enemyWidth = animationStates.get(0).getCurrentSprite().getWidth();
        enemyHeight = animationStates.get(0).getCurrentSprite().getHeight();

        stateMachine.addState("idle", "run", "startRun");
        stateMachine.addState("run", "idle", "stopRun");

        stateMachine.addState("idle", "damage", "Damage");
        stateMachine.addState("run", "damage", "Damage");
        stateMachine.addState("damage", "idle", "endDamage");

        stateMachine.addState("idle", "attack", "startAttack");
        stateMachine.addState("run", "attack", "startAttack");
        stateMachine.addState("attack", "idle", "stopAttack_idle");
        stateMachine.addState("attack", "idle", "stopAttack_run");

        stateMachine.addState("idle", "dead", "Dead");
        stateMachine.addState("run", "dead", "Dead");
        stateMachine.addState("attack", "dead", "Dead");
        stateMachine.addState("damage", "dead", "Dead");


        this.addComponent(stateMachine);
    }
}
