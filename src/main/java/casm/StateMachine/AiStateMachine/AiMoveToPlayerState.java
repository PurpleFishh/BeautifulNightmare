package casm.StateMachine.AiStateMachine;

import casm.ECS.Components.PositionComponent;
import casm.ECS.GameObject;
import casm.StateMachine.AfterStateEndsNotify;
import casm.StateMachine.UpdatableState;

/**
 * Not used.
 */
public class AiMoveToPlayerState implements UpdatableState {
    private String name;
    private GameObject gameObject;

    public AiMoveToPlayerState(GameObject gameObject, String name) {
        this.name = name;
        this.gameObject = gameObject;
    }

    public void update(AfterStateEndsNotify notifier) {
        gameObject.getComponent(PositionComponent.class).velocity.x = 0;
        gameObject.getComponent(PositionComponent.class).sign.x = 0;
    }

    @Override
    public String getName() {
        return null;
    }
}
