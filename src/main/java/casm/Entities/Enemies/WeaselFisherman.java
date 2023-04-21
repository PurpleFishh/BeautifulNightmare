package casm.Entities.Enemies;

import casm.ECS.Components.AnimationStateMachine;
import casm.Entities.Enemies.Enemy;
import casm.SpriteUtils.Animation.AnimationState;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.Utils.Vector2D;

import java.util.List;

public class WeaselFisherman extends Enemy {

    private int enemyWidth, enemyHeight;
    public WeaselFisherman(Vector2D spawnPosition) {
        super(spawnPosition, 0, 0);
        initiate();
    }

    public WeaselFisherman(String name, Vector2D spawnPosition) {
        super(name, spawnPosition, 0, 0);
        initiate();
    }

    public void initiate()
    {
        generateAnimationStateMachine();
        updateDimensions(enemyWidth, enemyHeight);
        this.setDamage(10);
        this.setLife(55);

        this.init();
    }
    private void generateAnimationStateMachine() {
        AnimationStateMachine stateMachine = new AnimationStateMachine();
        List<AnimationState> animationStates = AnimationsExtract.extractAnimations("enemy_weasel_animation.json");

        animationStates.forEach(stateMachine::addState);
        stateMachine.setDefaultState(animationStates.get(0).getName());
        enemyWidth = animationStates.get(0).getCurrentSprite().getWidth();
        enemyHeight = animationStates.get(0).getCurrentSprite().getHeight();

        stateMachine.addState("idle", "run", "startRun");
        stateMachine.addState("run", "idle", "stopRun");
        stateMachine.addState("idle", "damage", "Damage");
        stateMachine.addState("run", "damage", "Damage");
        stateMachine.addState("damage", "idle", "endDamage");
        stateMachine.addState("idle", "dead", "Dead");
        stateMachine.addState("run", "dead", "Dead");


        this.addComponent(stateMachine);
    }
}
