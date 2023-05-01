package casm.StateMachine;

import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;

import java.util.Objects;

public class StateTrigger {
    public String state, trigger;

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
