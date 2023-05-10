package casm.Objects.Entities;

import casm.ECS.Components.AttackComponent;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.Objects.Object;
import casm.StateMachine.AfterStateEndsNotify;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Vector2D;

public class Entity extends Object implements AfterStateEndsNotify {

    private double life = 100;
    private double damage = 5;

    public Entity(String name, Vector2D spawnPosition, int entityWidth, int entityHeight, ColliderType colliderType,
                  int colliderWidth, int colliderHeight) {
        super(name, spawnPosition, entityWidth, entityHeight);

        entityInit(colliderType, colliderWidth, colliderHeight);
    }

    public Entity(String name, Vector2D spawnPosition, int entityWidth, int entityHeight, ColliderType colliderType,
                  int colliderWidth, int colliderHeight, double life, double damage) {
        super(name, spawnPosition, entityWidth, entityHeight);

        this.damage = damage;
        this.life = life;
        entityInit(colliderType, colliderWidth, colliderHeight);
    }

    private void entityInit(ColliderType colliderType, int colliderWidth, int colliderHeight) {
        this.getComponent(PositionComponent.class).setGravity(true);
        this.addComponent(new SpriteComponent());
        this.addComponent(new ColliderComponent(colliderType, colliderWidth, colliderHeight));
        this.addComponent(new DyncamicColliderComponent());
        this.addComponent(new AttackComponent());

        //this.init();
    }

    protected void updateDimensions(int width, int height) {
        super.updateDimensions(width, height);
        this.getComponent(ColliderComponent.class).updateColliderDimensions(ColliderType.ENTITY, width - 2, height - 2);
    }

    private void death() {
        this.getComponent(PositionComponent.class).setCanMove(false);
        this.getComponent(AnimationStateMachine.class).trigger("Dead", this);
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
        if (life <= 0)
            death();
    }

    public void damage(double damage) {
        this.life -= damage;
        if (life < 0)
            death();
    }

    public void revive(double health) {
        this.life += health;
    }

    @Override
    public void afterStateEndsNotify() {
        //((LeveleScene)Game.getCurrentScene()).removeEntity(this);
        //TODO: Fa cu un timer sa se despauneze in timp nu instant dupa animatie
        this.kill();
    }
}

