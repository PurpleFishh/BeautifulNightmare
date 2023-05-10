package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.Rectangle;
import casm.Objects.Entities.Enemies.Enemy;
import casm.Objects.Entities.Player;
import casm.Game;
import casm.Scenes.LeveleScene;
import casm.StateMachine.AfterStateEndsNotify;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Settings.Setting;

public class AttackComponent extends Component implements AfterStateEndsNotify {


    private Rectangle attackCollider, playerCollider;
    private boolean isFlippedVertically = false, isFlippedHorizontally = false;
    private boolean isAttacking = false;
    private double attackDelayDefalut = 10L, attackDelay = attackDelayDefalut;

    public AttackComponent() {

    }

    @Override
    public void init() {
        playerCollider = gameObject.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY);
        attackCollider = gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.ATTACKING_BOX,
                0, 3, 30 + (int) playerCollider.getWidth(), 20);
//        attackCollider = gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.ATTACKING_BOX,
//                (int) playerCollider.getWidth(), 3, 30 , 20);
    }

    @Override
    public void update() {
        attackDelay -= 1 / Setting.DELTA_TIME;
    }

    public void attack() {
        if (attackDelay <= 0) {
            isAttacking = true;
            gameObject.getComponent(AnimationStateMachine.class).trigger("startAttack", this);
            if (this.gameObject == ((LeveleScene) Game.getCurrentScene()).getPlayer()) {
                Player player = (Player) gameObject;
                for (Enemy enemy : ((LeveleScene) Game.getCurrentScene()).getEnemies()) {
                    if (attackCollider.intersects(enemy.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY))) {
                        if (enemy.isAlive()) {
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
                    if (player.isAlive()) {
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
            attackDelay = attackDelayDefalut;
        }
    }

    public void afterStateEndsNotify() {
        if (gameObject.getComponent(PositionComponent.class).sign.x == 0)
            gameObject.getComponent(AnimationStateMachine.class).trigger("stopAttack_idle");
        else
            gameObject.getComponent(AnimationStateMachine.class).trigger("stopAttack_run");
        //gameObject.getComponent(PositionComponent.class).setCanMove(true);
        isAttacking = false;
    }

    public void setFlipColliderVertically(boolean flip) {
        if (flip != isFlippedVertically) {
            if (flip)
                attackCollider.getOffset().x -= attackCollider.getWidth() - playerCollider.getWidth();
            else
                attackCollider.getOffset().x += attackCollider.getWidth() - playerCollider.getWidth();
            isFlippedVertically = flip;
        }
    }

    public void setFlipColliderHorizontally(boolean flip) {
        if (flip != isFlippedHorizontally) {
            if (flip)
                attackCollider.getOffset().y -= playerCollider.getHeight() + attackCollider.getHeight();
            else
                attackCollider.getOffset().y += playerCollider.getHeight() + attackCollider.getHeight();
            isFlippedHorizontally = flip;
        }
    }

    public Rectangle getAttackCollider() {
        return attackCollider;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public double getAttackDelay() {
        return attackDelay;
    }

    public void setAttackDelay(double attackDelay) {
        this.attackDelayDefalut = attackDelay;
    }
}
