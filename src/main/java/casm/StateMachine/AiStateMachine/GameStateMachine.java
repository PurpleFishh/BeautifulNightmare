package casm.StateMachine.AiStateMachine;

import casm.Factory.SceneFactory.SceneFactory;
import casm.Scenes.Scene;
import casm.Scenes.SceneType;
import casm.StateMachine.AnimationStateMachine.State;
import casm.StateMachine.StateMachine;
import casm.StateMachine.StateTrigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Not used
 */
public class GameStateMachine implements StateMachine {
    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<SceneType> states = new ArrayList<>();
    private State currentState = null;
    private String defaultStateTitle = "";
    private SceneFactory factory;

    public GameStateMachine() {
        factory = new SceneFactory();
    }

    public void setDefaultState(String stateTitle) {
        for (SceneType state : states) {
            if (state.name().equals(stateTitle)) {
                defaultStateTitle = stateTitle;
                if (currentState == null) {
                   // currentState = (State) factory.create(state);
                    ((Scene) currentState).init();
                }
            }
            return;
        }
        System.out.println("Error: Unable to find state: " + stateTitle);
    }

    public void addState(String from, String to, String onTrigger) {
        stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    public void addState(SceneType state) {
        states.add(state);
    }

    public void trigger(String trigger) {
        for (StateTrigger state : stateTransfers.keySet()) {
            if (state.state.equals(currentState.getName()) && state.trigger.equals(trigger)) {
                if (stateTransfers.get(state) != null) {
                    Scene oldScene = null;
                    oldScene = (Scene) currentState;
                   // currentState = (State) factory.create(SceneType.valueOf(stateTransfers.get(state)));
                    ((Scene)currentState).init();
                    if(oldScene != null)
                    {
                        oldScene.destroy();
                        oldScene = null;
                    }
                }
                return;
            }
        }
    }

//    public void init() {
//        for (SceneType state : states)
//            if (state.name().equals(defaultStateTitle)) {
//                currentState = state;
//                break;
//            }
//    }

    public Scene getCurrentScene() {
        return (Scene)currentState;
    }
}
