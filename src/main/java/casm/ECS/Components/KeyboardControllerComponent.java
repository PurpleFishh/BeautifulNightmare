package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.Components.Collision.MovementMediator;
import casm.Game;
import casm.Observer.ObserverKeyboard;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.FlipEntityMediator;
import casm.Utils.Mediator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Component used for keyboard controlling which sets the flags for keys and notify the mediator for movement
 */
public class KeyboardControllerComponent extends Component implements ObserverKeyboard {
    /**
     * <b>right</b> - a flag if the entity is moving to right or not<br>
     * <b>left</b> - a flag if the entity is moving to left or not<br>
     */
    public boolean right = false, left = false;
    /**
     * Flags for keys to get its state if it is pressed, realised or not in use
     */
    private KeyState wKey = KeyState.NOT_USED, spaceKey = KeyState.NOT_USED, fKey = KeyState.NOT_USED,
            aKey = KeyState.NOT_USED, dKey = KeyState.NOT_USED;
    /**
     * The animation state machine for triggering the needed animation
     */
    private AnimationStateMachine animationStateMachine;
    /**
     * The mediator for movement
     *
     * @see MovementMediator
     */
    private Mediator mediator;

    /**
     * Setting up the key listener
     */
    public KeyboardControllerComponent() {

    }

    /**
     * Initializing the mediator
     */
    @Override
    public void init() {
        KeyboardListener.getInstance().subscribe(this);

        animationStateMachine = gameObject.getComponent(AnimationStateMachine.class);
        mediator = MovementMediator.getInstance(gameObject);
    }

    @Override
    public void destroy() {
        Thread th = new Thread(() -> {
            KeyboardListener.getInstance().unsubscribe(this);
            super.destroy();
        });
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updating the component<br>
     * Triggering the needed animations
     */
    @Override
    public void update() {
        if (gameObject.hasComponent(DyncamicColliderComponent.class)) {
            if (gameObject.getComponent(PositionComponent.class).isJumping()) {
                animationStateMachine.trigger("startJump");
            } else if (right || left)
                animationStateMachine.trigger("stopJump_StartRun");
            else
                animationStateMachine.trigger("stopJump_StartIdle");
        }
    }

    /**
     * Setting the flag for the pressed keys and notifying the mediator
     *
     * @param e the event to be processed
     */

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || spaceKey == KeyState.PRESSED) {
            spaceKey = KeyState.PRESSED;
            mediator.notify(this);
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            wKey = KeyState.PRESSED;
            mediator.notify(this);
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            fKey = KeyState.PRESSED;
            mediator.notify(this);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            aKey = KeyState.PRESSED;
            mediator.notify(this);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            dKey = KeyState.PRESSED;
            mediator.notify(this);
        }
    }

    /**
     * Setting the flag for the released keys and notifying the mediator
     *
     * @param e the event to be processed
     */

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            spaceKey = KeyState.RELEASED;
        if (e.getKeyCode() == KeyEvent.VK_W) {
            wKey = KeyState.RELEASED;
            mediator.notify(this);
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            fKey = KeyState.RELEASED;
            mediator.notify(this);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            aKey = KeyState.RELEASED;
            mediator.notify(this);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            dKey = KeyState.RELEASED;
            mediator.notify(this);
        }
    }

    /**
     * @return get the state of W key
     */
    public KeyState getWKeyState() {
        return wKey;
    }

    /**
     * @return get the state of A key
     */
    public KeyState getAKeyState() {
        return aKey;
    }

    /**
     * @return get the state of D key
     */
    public KeyState getDKeyState() {
        return dKey;
    }

    /**
     * @return get the state of Space key
     */
    public KeyState getSpaceKeyState() {
        return spaceKey;
    }

    /**
     * Reset the state of W key(NOT_USED state)
     */
    public void resetWKeyState() {
        wKey = KeyState.NOT_USED;
    }

    /**
     * Reset the state of A key(NOT_USED state)
     */
    public void resetAKeyState() {
        aKey = KeyState.NOT_USED;
    }

    /**
     * Reset the state of D key(NOT_USED state)
     */
    public void resetDKeyState() {
        dKey = KeyState.NOT_USED;
    }

    /**
     * Reset the state of Space key(NOT_USED state)
     */
    public void resetSpaceKeyState() {
        spaceKey = KeyState.NOT_USED;
    }

    /**
     * @return get the state of F key
     */
    public KeyState getFKeyState() {
        return fKey;
    }

    /**
     * Reset the state of F key(NOT_USED state)
     */
    public void resetFKeyState() {
        fKey = KeyState.NOT_USED;
    }

    @Override
    public void notify(KeyEvent event) {
        if (event.getID() == KeyEvent.KEY_PRESSED)
            keyPressed(event);
        if (event.getID() == KeyEvent.KEY_RELEASED)
            keyReleased(event);
    }
}
