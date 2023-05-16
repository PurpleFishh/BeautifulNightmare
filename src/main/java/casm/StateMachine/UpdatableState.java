package casm.StateMachine;

import casm.StateMachine.AnimationStateMachine.State;

public interface UpdatableState extends State {
    void update(AfterStateEndsNotify notifier);
}
