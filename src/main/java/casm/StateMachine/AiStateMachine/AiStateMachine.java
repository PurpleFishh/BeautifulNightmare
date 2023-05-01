package casm.StateMachine.AiStateMachine;

import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;
import casm.StateMachine.AfterStateEndsNotify;
import casm.StateMachine.AnimationStateMachine.AnimationState;
import casm.StateMachine.State;
import casm.StateMachine.StateMachine;
import casm.StateMachine.StateTrigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AiStateMachine implements StateMachine {
    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<State> states = new ArrayList<>();
    private State currentState = null, playAfter = null;
    private String defaultStateTitle = "";
    private AfterStateEndsNotify afterAnimationNotifier = null;
    private GameObject gameObject;

    public AiStateMachine(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public void setDefaultState(String stateTitle) {
        for (State state : states) {
            if (state.getName().equals(stateTitle)) {
                defaultStateTitle = stateTitle;
                if (currentState == null)
                    currentState = state;
            }
            return;
        }
        System.out.println("Error: Unable to find state: " + stateTitle);
    }

    public void addState(String from, String to, String onTrigger) {
        stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    public void addState(State state) {
        states.add(state);
    }

    public void trigger(String trigger) {
        for (StateTrigger state : stateTransfers.keySet()) {
            if (state.state.equals(currentState.getName()) && state.trigger.equals(trigger)) {
                if (stateTransfers.get(state) != null) {
                    currentState = states.get(stateIndexOf(stateTransfers.get(state)));
                    currentState.update(null);
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
//                        if (!currentState.getDoseLoop())
//                            currentState.resetToFirstFrame();
//                        currentState = states.get(newStateIndex);
//                        afterAnimationNotifier = notifier;
                    }
                }
                return;
            }
        }
    }

//    public void trigger(String trigger, boolean playAfter) {
//        for (StateTrigger state : stateTransfers.keySet()) {
//            if (state.state.equals(currentState.getName()) && state.trigger.equals(trigger)) {
//                if (stateTransfers.get(state) != null) {
//                    int newStateIndex = stateIndexOf(stateTransfers.get(state));
//                    if (newStateIndex > -1) {
//                        if (!currentState.getDoseLoop())
//                            currentState.resetToFirstFrame();
//                        if (playAfter)
//                            this.playAfter = states.get(newStateIndex);
//                        else
//                            currentState = states.get(newStateIndex);
//                        afterAnimationNotifier = null;
//                    }
//                }
//                return;
//            }
//        }
//    }

    private int stateIndexOf(String stateTitle) {
        int index = 0;
        for (State state : states) {
            if (state.getName().equals(stateTitle))
                return index;
            index++;
        }
        return -1;
    }

    public void init() {
        for (State state : states)
            if (state.getName().equals(defaultStateTitle)) {
                currentState = state;
                break;
            }
    }


//    public void update() {
//        if (currentState != null) {
//            currentState.update(afterAnimationNotifier);
//            if (currentState.isEndedPlaying()) {
//                if (!currentState.getDoseLoop()) {
//                    currentState.resetToFirstFrame();
//                    afterAnimationNotifier = null;
//                }
//                if (playAfter != null) {
//                    currentState = playAfter;
//                    playAfter = null;
//                }
//            }
//            SpriteComponent spriteComponent = gameObject.getComponent(SpriteComponent.class);
//            if (spriteComponent != null) {
//                spriteComponent.setSprite(currentState.getCurrentSprite());
//            }
//        }
//    }
}
