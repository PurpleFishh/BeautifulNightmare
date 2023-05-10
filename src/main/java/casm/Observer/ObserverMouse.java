package casm.Observer;

import java.awt.event.MouseEvent;

public interface ObserverMouse extends Observer {
    void notify(MouseEvent event);
}
