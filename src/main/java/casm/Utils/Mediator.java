package casm.Utils;

import casm.ECS.Component;
import casm.ECS.Components.Collision.MovementMediator;

/**
 * Used to communicate components
 * Implements the mediator design pattern
 */
public interface Mediator {
    /**
     * Called by components to notify the mediator for an event
     *
     * @param component the component that notifies the mediator
     */
    void notify(Component component);
}
