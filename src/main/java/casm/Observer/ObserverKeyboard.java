package casm.Observer;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface ObserverKeyboard extends Observer{
    void notify(KeyEvent event);
}
