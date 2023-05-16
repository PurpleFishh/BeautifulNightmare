package casm.StateMachine;

public interface StateMachine {
    void setDefaultState(String defaultState);
    void addState(String from, String to, String onTrigger);
    void trigger(String trigger);
    //void trigger(String trigger, AfterStateEndsNotify notifier);
}
