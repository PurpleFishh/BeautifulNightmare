package casm.StateMachine;

public interface State {
    void update(AfterStateEndsNotify notifier);
    String getName();
}
