package casm.StateMachine;

/**
 * This interface is used for creating a state machine.
 */
public interface StateMachine {
    /**
     * This method is used to set the default state of the state machine.
     * @param defaultState The default state of the state machine.
     */
    void setDefaultState(String defaultState);

    /**
     * This method is used to add a state to the state machine.
     * @param from The state that we want to transfer from.
     * @param to The state that we want to transfer to.
     * @param onTrigger The trigger that will trigger the transfer.
     */
    void addState(String from, String to, String onTrigger);

    /**
     * Used to trigger a transfer between two states if the trigger is valid.
     * @param trigger The trigger that will trigger the transfer.
     */
    void trigger(String trigger);
    //void trigger(String trigger, AfterStateEndsNotify notifier);
}
