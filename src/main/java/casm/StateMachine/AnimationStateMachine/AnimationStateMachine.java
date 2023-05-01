package casm.StateMachine.AnimationStateMachine;

import casm.ECS.Component;
import casm.ECS.Components.SpriteComponent;
import casm.StateMachine.AfterStateEndsNotify;
import casm.StateMachine.StateMachine;
import casm.StateMachine.StateTrigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AnimationStateMachine extends Component implements StateMachine {

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private AnimationState currentState = null, playAfter = null;
    private String defaultStateTitle = "";
    private AfterStateEndsNotify afterAnimationNotifier = null;

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
                        if (!currentState.getDoseLoop())
                            currentState.resetToFirstFrame();
                        currentState = states.get(newStateIndex);
                        afterAnimationNotifier = null;
                    }
                }
                return;
            }
        }
    }

    public void trigger(String trigger, AfterStateEndsNotify notifier) {
        for (StateTrigger state : stateTransfers.keySet()) {
            if (state.state.equals(currentState.getName()) && state.trigger.equals(trigger)) {
                if (stateTransfers.get(state) != null) {
                    int newStateIndex = stateIndexOf(stateTransfers.get(state));
                    if (newStateIndex > -1) {
                        if (!currentState.getDoseLoop())
                            currentState.resetToFirstFrame();
                        currentState = states.get(newStateIndex);
                        afterAnimationNotifier = notifier;
                    }
                }
                return;
            }
        }
    }

    public void trigger(String trigger, boolean playAfter) {
        for (StateTrigger state : stateTransfers.keySet()) {
            if (state.state.equals(currentState.getName()) && state.trigger.equals(trigger)) {
                if (stateTransfers.get(state) != null) {
                    int newStateIndex = stateIndexOf(stateTransfers.get(state));
                    if (newStateIndex > -1) {
                        if (!currentState.getDoseLoop())
                            currentState.resetToFirstFrame();
                        if (playAfter)
                            this.playAfter = states.get(newStateIndex);
                        else
                            currentState = states.get(newStateIndex);
                        afterAnimationNotifier = null;
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
        if (currentState != null) {
            currentState.update(afterAnimationNotifier);
            if (currentState.isEndedPlaying()) {
                if (!currentState.getDoseLoop()) {
                    currentState.resetToFirstFrame();
                    afterAnimationNotifier = null;
                }
                if (playAfter != null) {
                    currentState = playAfter;
                    playAfter = null;
                }
            }
            SpriteComponent spriteComponent = gameObject.getComponent(SpriteComponent.class);
            if (spriteComponent != null) {
                spriteComponent.setSprite(currentState.getCurrentSprite());
            }
        }
    }
}
