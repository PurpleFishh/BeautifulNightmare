package casm.ECS.Components;

import casm.Observer.Observable;
import casm.Observer.Observer;
import casm.Observer.ObserverMouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MouseListener implements Observable, java.awt.event.MouseListener, MouseMotionListener {

    private List<ObserverMouse> subscribers = new ArrayList<ObserverMouse>();
    private final List<ObserverMouse> subscribersSync = Collections.synchronizedList(subscribers);
    private MouseEvent event;
    private static MouseListener instance;
    private boolean iteratorSync = false;

    private MouseListener() {
    }

    public static MouseListener getInstance() {
        if (instance == null)
            instance = new MouseListener();
        return instance;
    }

    @Override
    public synchronized void notifyAllSubs() {
        iteratorSync = true;
        for (ObserverMouse observerMouse : subscribersSync) observerMouse.notify(event);
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
        subscribersSync.add((ObserverMouse) subscriber);
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
        subscribersSync.remove((ObserverMouse) subscriber);
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
    public void mouseClicked(MouseEvent e) {
        event = e;
        Thread th = new Thread(this::notifyAllSubs);
        th.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }


}
