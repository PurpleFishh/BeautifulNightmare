package casm.ECS.Components;

import casm.Observer.Observable;
import casm.Observer.Observer;
import casm.Observer.ObserverKeyboard;
import casm.Observer.ObserverMouse;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class KeyboardListener implements Observable, KeyListener {
    private List<ObserverKeyboard> subscribers = new ArrayList<>();
    private final List<ObserverKeyboard> subscribersSync = Collections.synchronizedList(subscribers);
    private KeyEvent event;
    private static KeyboardListener instance;
    private volatile Boolean iteratorSync = false;

    private KeyboardListener() {
    }

    public static KeyboardListener getInstance() {
        if (instance == null)
            instance = new KeyboardListener();
        return instance;
    }

    @Override
    public synchronized void notifyAllSubs() {
        iteratorSync = true;
        for (ObserverKeyboard observerKeyboard : subscribersSync) observerKeyboard.notify(event);
        iteratorSync = false;
        notifyAll();
    }

    @Override
    public synchronized void subscribe(Observer subscriber) {
        while (iteratorSync) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread Interrupted");
            }
        }
        subscribersSync.add((ObserverKeyboard) subscriber);
        notifyAll();
    }

    @Override
    public synchronized void unsubscribe(Observer subscriber) {
        while (iteratorSync) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread Interrupted");
            }
        }
        subscribersSync.remove((ObserverKeyboard) subscriber);
        notifyAll();
    }

    @Override
    public synchronized void unsubscribeAll() {
        while (iteratorSync) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread Interrupted");
            }
        }
        subscribersSync.clear();
        notifyAll();
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        event = e;
        Thread th = new Thread(this::notifyAllSubs);
        th.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        event = e;
        Thread th = new Thread(this::notifyAllSubs);
        th.start();
    }
}
