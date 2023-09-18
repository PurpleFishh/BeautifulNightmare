package casm.StateMachine;

import java.util.Objects;

/**
 * This class is used to transfer from one state of the animation to another.
 */
public class StateTrigger {
    /**
     * <b>state</b> - is the state of the animation that we want to transfer from.
     * <b>trigger</b> - is the key string that triggers the animation to transfer to another state.
     */
    public String state, trigger;

    /**
     * @param state the state of the animation
     * @param trigger the trigger of the animation to transfer to another state
     */
    public StateTrigger(String state, String trigger) {
        this.state = state;
        this.trigger = trigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateTrigger that = (StateTrigger) o;
        return Objects.equals(state, that.state) && Objects.equals(trigger, that.trigger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, trigger);
    }
}
