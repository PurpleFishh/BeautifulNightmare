package casm.Objects.Entities;

import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.Game;
import casm.Objects.Object;
import casm.Scenes.SceneType;
import casm.StateMachine.AfterStateEndsNotify;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Vector2D;

/**
 * The entity class is the base class for all the entities in the game
 */
public class Entity extends Object implements AfterStateEndsNotify {

    /**
     * The life of the entity
     */
    private double life = 100;
    /**
     * Entity's attack damage
     */
    private double damage = 5;

    /**
     * @param name           name for the entity
     * @param spawnPosition  position for the entity
     * @param entityWidth    width for the entity
     * @param entityHeight   height for the entity
     * @param colliderType   type of the collider
     * @param colliderWidth  width for the collider
     * @param colliderHeight height for the collider
     */
    public Entity(String name, Vector2D spawnPosition, int entityWidth, int entityHeight, ColliderType colliderType,
                  int colliderWidth, int colliderHeight) {
        super(name, spawnPosition, entityWidth, entityHeight);

        entityInit(colliderType, colliderWidth, colliderHeight);
    }

    /**
     * @param name           name for the entity
     * @param spawnPosition  position for the entity
     * @param entityWidth    width for the entity
     * @param entityHeight   height for the entity
     * @param colliderType   type of the collider
     * @param colliderWidth  width for the collider
     * @param colliderHeight height for the collider
     * @param life           life for the entity
     * @param damage         damage for the entity
     */
    public Entity(String name, Vector2D spawnPosition, int entityWidth, int entityHeight, ColliderType colliderType,
                  int colliderWidth, int colliderHeight, double life, double damage) {
        super(name, spawnPosition, entityWidth, entityHeight);

        this.damage = damage;
        this.life = life;
        entityInit(colliderType, colliderWidth, colliderHeight);
    }

    /**
     * Initialize the entity
     * @param colliderType  type of the collider
     * @param colliderWidth width for the collider
     * @param colliderHeight height for the collider
     */
    private void entityInit(ColliderType colliderType, int colliderWidth, int colliderHeight) {
        this.getComponent(PositionComponent.class).setGravity(true);
        this.addComponent(new SpriteComponent());
        this.addComponent(new ColliderComponent(colliderType, colliderWidth, colliderHeight));
        this.addComponent(new DyncamicColliderComponent());

        //this.init();
    }

    /**
     * Update the dimensions of the entity
     * @param width  new width for the entity
     * @param height new height for the entity
     */
    protected void updateDimensions(int width, int height) {
        super.updateDimensions(width, height);
        this.getComponent(ColliderComponent.class).updateColliderDimensions(ColliderType.ENTITY, width - 2, height - 2);
    }

    /**
     * Used when the entity was killed
     */
    private void death() {
        this.getComponent(PositionComponent.class).setCanMove(false);
        this.getComponent(AnimationStateMachine.class).trigger("Dead", this);
    }

    /**
     * @return entity's attack damage
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Set the entity's attack damage
     * @param damage new attack damage for the entity
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * @return entity health
     */
    public double getLife() {
        return life;
    }

    /**
     * Set the entity's health
     * @param life new health for the entity
     */
    public void setLife(double life) {
        this.life = life;
        if (life <= 0)
            death();
    }

    /**
     * Apply damage to the entity
     * @param damage damage to be applied to the entity
     */
    public void damage(double damage) {
        this.life -= damage;
        if (life <= 0)
            death();
    }

    /**
     * Revive the entity
     * @param health health to be applied to the entity
     */
    public void revive(double health) {
        this.life = (health + life) > 100 ? 100 : (health + life);
    }

    /**
     * The Method is called after the death animation ends, so the entity can be removed from the game
     */
    @Override
    public void afterStateEndsNotify() {
        //TODO: Fa cu un timer sa se despauneze in timp nu instant dupa animatie
        this.kill();
        if (this instanceof Player) {
            Game.changeScene(SceneType.GAME_OVER_MENU);
            //Game.destroyAllWithoutTopScenes();
        }
    }
}

