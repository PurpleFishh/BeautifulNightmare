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

/**
 * The mechanism used for the animations of every object
 * This class is used to store the animation states and transfer between them.
 */
public class AnimationStateMachine extends Component implements StateMachine {

    /**
     * Mapping the animation state, its trigger({@link StateTrigger}) and the animation that will be triggered to
     */
    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    /**
     * All the animation states
     */
    private final List<AnimationState> states = new ArrayList<>();
    /**
     * <b>currentState</b> - the current animation state that is playing.
     * <b>playAfter</b> - if the next state of animation should play after the current one ends or the transition should be made instant.
     */
    private AnimationState currentState = null, playAfter = null;
    /**
     * The default animation state name.
     */
    private String defaultStateTitle = "";
    /**
     * The method that will be called after the animation ends(if not null).
     */
    private AfterStateEndsNotify afterAnimationNotifier = null;

    /**
     * Set the default animation state.
     *
     * @param animationTitle The title of the animation state.
     */
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

    /**
     * Add a transition between two animation states.
     *
     * @param from      The animation state that the transition starts from.
     * @param to        The animation state that the transition ends at.
     * @param onTrigger The trigger that will trigger the transition.
     */
    public void addState(String from, String to, String onTrigger) {
        stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    /**
     * Add a new animation state.
     *
     * @param state The animation state to be added.
     */
    public void addState(AnimationState state) {
        states.add(state);
    }

    /**
     * Trigger form the current animation to another one if the given trigger exists.
     *
     * @param trigger trigger to another animation
     */
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

    /**
     * Trigger form the current animation to another one if the given trigger exists.<br>
     * After the animation ends, the given method will be called(if not null).
     *
     * @param trigger  trigger to another animation
     * @param notifier the method that will be called after the animation ends
     */
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

    /**
     * Trigger form the current animation to another one if the given trigger exists.<br>
     * The transition will be made after the current animation ends playing
     *
     * @param trigger trigger to another animation
     * @param playAfter if the next animations should be played after the current one ends
     */
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

    /**
     * @param stateTitle The title of the animation state.
     * @return The index of the animation state in the list of states.
     */
    private int stateIndexOf(String stateTitle) {
        int index = 0;
        for (AnimationState state : states) {
            if (state.getName().equals(stateTitle))
                return index;
            index++;
        }
        return -1;
    }

    /**
     * Initialize the animation state machine and its states.
     */
    @Override
    public void init() {
        for (AnimationState state : states)
            if (state.getName().equals(defaultStateTitle)) {
                currentState = state;
                break;
            }
    }

    /**
     * Update the animation state.<br>
     * Change frames, update them and change the current sprite of the {@link SpriteComponent}
     */
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
