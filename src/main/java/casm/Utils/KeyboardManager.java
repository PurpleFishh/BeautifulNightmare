package casm.Utils;

import casm.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardManager implements KeyListener {

    private static boolean[] keys;

    public KeyboardManager()
    {
        keys = new boolean[256];
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        keys[e.getKeyCode()] = true;
        Game.getCurrentScene().eventHandler();
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        keys[e.getKeyCode()] = false;
        Game.getCurrentScene().eventHandler();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    public static boolean isUp() {
        return keys[KeyEvent.VK_W];
    }

    public static boolean isDown() {
        return keys[KeyEvent.VK_S];
    }

    public static boolean isLeft() {
        return keys[KeyEvent.VK_A];
    }

    public static boolean isRight() {
        return keys[KeyEvent.VK_D];
    }
}
