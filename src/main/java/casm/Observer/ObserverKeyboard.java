package casm.Observer;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Object notified when a keyboard event happens
 */
public interface ObserverKeyboard extends Observer{
    void notify(KeyEvent event);
}
