package casm.Utils;

import casm.ECS.Component;

public interface Mediator {
    void notify(Component component);
}
