package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.Rectangle;
import casm.Entities.Enemies.Enemy;
import casm.Entities.Player;
import casm.Game;
import casm.Scenes.LeveleScene;
import casm.SpriteUtils.Animation.AnimationEndNotify;

public class AttackComponent extends Component implements AnimationEndNotify {


    private Rectangle attackCollider, playerCollider;
    private boolean isFlippedHorizontally = false, isFlippedVertically = false;

    public AttackComponent() {

    }

    @Override
    public void init() {
        playerCollider = gameObject.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY);
        attackCollider = gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.ATTACKING_BOX, (int) playerCollider.getWidth(), 3, 30, 20);
    }

    @Override
    public void update() {
        //gameObject.getComponent(SpriteComponent.class).f
    }

    public void attack() {
        gameObject.getComponent(AnimationStateMachine.class).trigger("startAttack", this);
        if (this.gameObject == ((LeveleScene) Game.getCurrentScene()).getPlayer()) {
            Player player = (Player) gameObject;
            for (Enemy enemy : ((LeveleScene) Game.getCurrentScene()).getEnemies()) {
                if (attackCollider.intersects(enemy.getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY))) {
                    enemy.damage(player.getDamage());
                    System.out.println(enemy.getLife());
                    if (enemy.isAlive()) {
                        enemy.getComponent(AnimationStateMachine.class).trigger("Damage");
                        enemy.getComponent(AnimationStateMachine.class).trigger("endDamage", true);
                    }
                }
            }
        } else {
            attackCollider.intersects(((LeveleScene) Game.getCurrentScene()).getPlayer().getComponent(ColliderComponent.class).getCollider(ColliderType.ENTITY));
        }
    }

    public void animationEndNotify() {
        gameObject.getComponent(AnimationStateMachine.class).trigger("stopAttack");
    }

    public void setFlipColliderHorizontally(boolean flip) {
        if (flip != isFlippedHorizontally) {
            if (flip)
                attackCollider.getOffset().x -= playerCollider.getWidth() + attackCollider.getWidth();
            else
                attackCollider.getOffset().x += playerCollider.getWidth() + attackCollider.getWidth();
            isFlippedHorizontally = flip;
        }
    }

    public void setFlipColliderVertically(boolean flip) {
        if (flip != isFlippedVertically) {
            if (flip)
                attackCollider.getOffset().y -= playerCollider.getHeight() + attackCollider.getHeight();
            else
                attackCollider.getOffset().y += playerCollider.getHeight() + attackCollider.getHeight();
            isFlippedVertically = flip;
        }
    }

}
