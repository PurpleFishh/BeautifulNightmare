package casm.ECS.Components;

import casm.ECS.Component;
import casm.SpriteUtils.Animation.AnimationState;
import casm.SpriteUtils.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AnimationStateMachine extends Component {

    private class StateTrigger {
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

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private AnimationState currentState = null;
    private String defaultStateTitle = "";

    public void setDefaultState(String animationTitle) {
        for (AnimationState state : states) {
            if (state.getName().equals(animationTitle)) {
                defaultStateTitle = animationTitle;
                if (currentState == null)
                    currentState = state;
            }
            return;
        }
        System.out.println("Error: Unable to find state: " + animationTitle);
    }

    public void addState(String from, String to, String onTrigger) {
        stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    public void addState(AnimationState state) {
        states.add(state);
    }

    public void trigger(String trigger) {
        for (StateTrigger state : stateTransfers.keySet()) {
            if (state.state.equals(currentState.getName()) && state.trigger.equals(trigger)) {
                if (stateTransfers.get(state) != null) {
                    int newStateIndex = stateIndexOf(stateTransfers.get(state));
                    if (newStateIndex > -1) {
                        if(!currentState.getDoseLoop())
                            currentState.resetToFirstFrame();
                        currentState = states.get(newStateIndex);
                    }
                }
                return;
            }
        }
    }

    private int stateIndexOf(String stateTitle) {
        int index = 0;
        for (AnimationState state : states) {
            if (state.getName().equals(stateTitle))
                return index;
            index++;
        }
        return -1;
    }

    @Override
    public void init() {
        for (AnimationState state : states)
            if (state.getName().equals(defaultStateTitle)) {
                currentState = state;
                break;
            }
    }

    @Override
    public void update() {
        if(currentState != null){
            currentState.update();
            SpriteComponent spriteComponent = gameObject.getComponent(SpriteComponent.class);
            if(spriteComponent != null)
            {
                spriteComponent.setSprite(currentState.getCurrentSprite());
            }
        }
    }
}
