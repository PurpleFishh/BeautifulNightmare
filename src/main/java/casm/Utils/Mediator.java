package casm.Utils;

import casm.ECS.Component;
import casm.ECS.Components.Collision.MovementMediator;

public interface Mediator {
    void notify(Component component);
}
