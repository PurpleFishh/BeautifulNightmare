package casm.StateMachine.AiStateMachine;

import casm.ECS.Components.PositionComponent;
import casm.ECS.GameObject;
import casm.StateMachine.AfterStateEndsNotify;
import casm.StateMachine.State;

public class AiMoveToPlayerState implements State {
    private String name;
    private GameObject gameObject;

    public AiMoveToPlayerState(GameObject gameObject, String name) {
        this.name = name;
        this.gameObject = gameObject;
    }
    @Override
    public void update(AfterStateEndsNotify notifier) {
        gameObject.getComponent(PositionComponent.class).velocity.x = 0;
        gameObject.getComponent(PositionComponent.class).sign.x = 0;
    }

    @Override
    public String getName() {
        return null;
    }
}
