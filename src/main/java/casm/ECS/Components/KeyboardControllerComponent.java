package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardControllerComponent extends Component implements KeyListener {

    private boolean right = false, left = false, up, down;
    private PositionComponent positionComponent;
    private SpriteComponent spriteComponent;
    private AnimationStateMachine animationStateMachine;

    public KeyboardControllerComponent() {
        Game.getWindow().getWndFrame().addKeyListener(this);
    }

    @Override
    public void init() {
        positionComponent = gameObject.getComponent(PositionComponent.class);
        spriteComponent = gameObject.getComponent(SpriteComponent.class);
        animationStateMachine = gameObject.getComponent(AnimationStateMachine.class);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void update() {
        if (gameObject.getComponent(DyncamicColliderComponent.class) != null)
            if (!gameObject.getComponent(DyncamicColliderComponent.class).isOnGround()) {
                animationStateMachine.trigger("startJump");
            } else if (right || left)
                animationStateMachine.trigger("stopJump_StartRun");
            else
                animationStateMachine.trigger("stopJump_StartIdle");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameObject.getComponent(DyncamicColliderComponent.class) != null)
                if (gameObject.getComponent(DyncamicColliderComponent.class).isOnGround()) {
                    positionComponent.sign.y = -1;
                    //animationStateMachine.trigger("startJump");
                }
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (!right)
                positionComponent.sign.x = -1;
            left = true;
            spriteComponent.setFlippedVertically(true);
            animationStateMachine.trigger("startRun");
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if (!left) {
                positionComponent.sign.x = 1;
            }
            right = true;
            animationStateMachine.trigger("startRun");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (right) {
                if (!(positionComponent.velocity.x > 0)) {
                    positionComponent.velocity.x *= -1;
                    //positionComponent.velocity.x /= 1.5;
                }
            } else {
                positionComponent.velocity.x = 0;
                animationStateMachine.trigger("stopRun");
            }
            positionComponent.sign.x = 0;
            left = false;
            spriteComponent.setFlippedVertically(false);

        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if (left) {
                if (!(positionComponent.velocity.x < 0)) {
                    positionComponent.velocity.x *= -1;
                    //positionComponent.velocity.x /= 1.5;
                }
            } else {
                positionComponent.velocity.x = 0;
                animationStateMachine.trigger("stopRun");
            }
            positionComponent.sign.x = 0;
            right = false;

        }
    }
}
