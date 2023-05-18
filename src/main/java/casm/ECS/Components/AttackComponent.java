package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.Rectangle;
import casm.Factory.EntityFactory.EntityType;
import casm.Objects.Entities.Enemies.Enemy;
import casm.Objects.Entities.Player;
import casm.Game;
import casm.Scenes.Level.LeveleScene;
import casm.StateMachine.AfterStateEndsNotify;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Settings.Setting;

/**
 * The Attack component used for entities when we want to make a attack
 */
public class AttackComponent extends Component implements AfterStateEndsNotify {

    /**
     * attackCollider - the attack hit box, where the entity can hit
     * playerCollider - entity hit box
     */
    private Rectangle attackCollider, playerCollider;
    /**
     * If the entity is flipped or not
     */
    private boolean isFlippedVertically = false, isFlippedHorizontally = false;
    /**
     * If the entity is in attack or not
     */
    private boolean isAttacking = false;
    /**
     * attackDelayDefault - the default delay between attacks
     * attackDelay - the attack between attacks
     */
    private double attackDelayDefault = 10L, attackDelay = 0L;
    private EntityType type;

    public AttackComponent() {

    }
    public AttackComponent(EntityType type) {
        this.type = type;
    }

    /**
     * Getting the references of the components
     */
    @Override
    public void init() {
        playerCollider = gameObject.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY);
        switch (type)
        {
            case CATFISH -> attackCollider = gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.ATTACKING_BOX,
                    0, 13, 5 + (int) playerCollider.getWidth(), 23);
            case WEASEL_FISHERMAN ->  attackCollider = gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.ATTACKING_BOX,
                    0, 3, 20 + (int) playerCollider.getWidth(), 33);
            default ->  attackCollider = gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.ATTACKING_BOX,
                    0, 3, 30 + (int) playerCollider.getWidth(), 20);
        }

//        attackCollider = gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.ATTACKING_BOX,
//                (int) playerCollider.getWidth(), 3, 30 , 20);
    }

    /**
     * Subbing the attack delay
     */
    @Override
    public void update() {
        attackDelay -= 1 / Setting.DELTA_TIME;
    }

    /**
     * Used when the entity attacks,
     * it will check for player and enemies if the attack hit box is in contact with any entity it will damage it
     */
    public void attack() {
        if (attackDelay <= 0) {
            isAttacking = true;
            if (this.gameObject == ((LeveleScene) Game.getCurrentScene()).getPlayer()) {
                gameObject.getComponent(AnimationStateMachine.class).trigger("startAttack", this);
                Player player = (Player) gameObject;
                for (Enemy enemy : ((LeveleScene) Game.getCurrentScene()).getEnemies()) {
                    if (attackCollider.intersects(enemy.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY))) {
                        if (enemy.getLife() > 0) {
                            enemy.damage(player.getDamage());
                            if (enemy.isAlive()) {
                                enemy.getComponent(AnimationStateMachine.class).trigger("Damage", this);
                                enemy.getComponent(AnimationStateMachine.class).trigger("endDamage", true);
                                //enemy.getComponent(PositionComponent.class).setCanMove(false);
                            }
                        }
                    }
                }
            } else {
                Player player = ((LeveleScene) Game.getCurrentScene()).getPlayer();
                if (attackCollider.intersects(player.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY))) {
                    if (player.getLife() > 0) {
                        gameObject.getComponent(AnimationStateMachine.class).trigger("startAttack", this);
                        player.damage(((Enemy) gameObject).getDamage());
                        System.out.println(player.getLife());
                        if (player.isAlive()) {
                            player.getComponent(AnimationStateMachine.class).trigger("Damage");
                            player.getComponent(AnimationStateMachine.class).trigger("endDamage", true);
                        }
                        System.out.println("Attack");
                    }
                }
            }
            attackDelay = attackDelayDefault;
        }
    }

    /**
     * After the attack ends it is the need to reset the animations and the variables
     */
    public void afterStateEndsNotify() {
        if (gameObject.getComponent(PositionComponent.class).sign.x == 0)
            gameObject.getComponent(AnimationStateMachine.class).trigger("stopAttack_idle");
        else
            gameObject.getComponent(AnimationStateMachine.class).trigger("stopAttack_run");
        //gameObject.getComponent(PositionComponent.class).setCanMove(true);
        isAttacking = false;
    }

    /**
     * Flip the entity and the collider
     *
     * @param flip if it is flipped or not
     */
    public void setFlipColliderVertically(boolean flip) {
        if (flip != isFlippedVertically) {
            if (flip)
                attackCollider.getOffset().x -= attackCollider.getWidth() - playerCollider.getWidth();
            else
                attackCollider.getOffset().x += attackCollider.getWidth() - playerCollider.getWidth();
            isFlippedVertically = flip;
        }
    }

    /**
     * Flip the entity and the collider
     *
     * @param flip if it is flipped or not
     */
    public void setFlipColliderHorizontally(boolean flip) {
        if (flip != isFlippedHorizontally) {
            if (flip)
                attackCollider.getOffset().y -= playerCollider.getHeight() + attackCollider.getHeight();
            else
                attackCollider.getOffset().y += playerCollider.getHeight() + attackCollider.getHeight();
            isFlippedHorizontally = flip;
        }
    }

    /**
     * @return get the attack collider hit box
     */
    public Rectangle getAttackCollider() {
        return attackCollider;
    }

    /**
     * @return if the entity is in attack or not
     */
    public boolean isAttacking() {
        return isAttacking;
    }

    /**
     * @return get the attack delay
     */
    public double getAttackDelay() {
        return attackDelay;
    }

    /**
     * @param attackDelay set a new attack delay
     */
    public void setAttackDelay(double attackDelay) {
        this.attackDelayDefault = attackDelay;
    }
}
