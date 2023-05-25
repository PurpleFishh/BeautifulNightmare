package casm.Observer;

import java.awt.event.MouseEvent;

/**
 * Object notified when a mouse event happens
 */
public interface ObserverMouse extends Observer {
    void notify(MouseEvent event);
}
