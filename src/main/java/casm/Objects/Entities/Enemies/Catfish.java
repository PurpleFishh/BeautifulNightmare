package casm.Objects.Entities.Enemies;

import casm.ECS.Components.AttackComponent;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.Rectangle;
import casm.ECS.Components.PositionComponent;
import casm.Factory.EntityFactory.EntityType;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.StateMachine.AnimationStateMachine.AnimationState;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Settings.EntitiesSettings;
import casm.Utils.Vector2D;

import java.util.List;

public class Catfish extends Enemy {

    private int enemyWidth, enemyHeight;

    public Catfish(Vector2D spawnPosition) {
        super(EntityType.CATFISH, spawnPosition, 0, 0);
        initiate();
    }

    public Catfish(String name, Vector2D spawnPosition) {
        super(name, EntityType.CATFISH, spawnPosition, 0, 0);
        initiate();
    }

    public void initiate() {
        generateAnimationStateMachine();
        this.getComponent(PositionComponent.class).setMaxSpeed(EntitiesSettings.CatfishInfo.CATFISH_MAX_SPEED);
        this.getComponent(AttackComponent.class).setAttackDelay(6L);
        updateDimensions(enemyWidth, enemyHeight);
        this.setDamage(16);
        this.setLife(40);


        // this.init();
    }

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
