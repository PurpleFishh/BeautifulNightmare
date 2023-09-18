package casm.ECS.Components;

import casm.Game;
import casm.Observer.Observable;
import casm.Observer.Observer;
import casm.Observer.ObserverKeyboard;
import casm.Observer.ObserverMouse;
import casm.Scenes.Scene;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An observer that listens to all the mouse events
 */
public class MouseListener implements Observable, java.awt.event.MouseListener, MouseMotionListener {
    /**
     * The subscribers of the observer
     */
    private final HashMap<Scene, List<ObserverMouse>> subscribersSync = new HashMap<>();
    /**
     * Used for synchronizing the access to the list
     */
    //private final List<ObserverMouse> subscribersSync = Collections.synchronizedList(subscribers);
    /**
     * The event that needs to be sent to the {@link ObserverMouse}
     */
    private MouseEvent event;
    /**
     * The instance of the singleton
     */
    private static MouseListener instance;
    /**
     * Used for synchronizing the access to the list(a semaphore in the access thread)
     */
    private final boolean iteratorSync = false;

    private MouseListener() {
    }

    /**
     * @return get the instance of the singleton
     */
    public static MouseListener getInstance() {
        if (instance == null)
            instance = new MouseListener();
        return instance;
    }

    /**
     * Notify all the subscribers of the event that happened
     */
    @Override
    public synchronized void notifyAllSubs() {
        if (Game.getCurrentScene().isEmpty())
            return;
        // iteratorSync = true;
        if (subscribersSync.containsKey(Game.getCurrentScene().get())) {
            List<ObserverMouse> copyList = new ArrayList<>(subscribersSync.get(Game.getCurrentScene().get()));
            for (ObserverMouse observerMouse : copyList) observerMouse.notify(event);
        }
        // iteratorSync = false;
        //  notifyAll();
    }

    /**
     * Subscribe a new {@link ObserverMouse}
     *
     * @param subscriber the new subscriber
     */
    @Override
    public synchronized void subscribe(Scene scene, Observer subscriber) {
//        while (iteratorSync) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                System.err.println("Thread Interrupted");
//            }
//        }
        if (subscribersSync.containsKey(scene))
            subscribersSync.get(scene).add((ObserverMouse) subscriber);
        else {
            ArrayList<ObserverMouse> list = new ArrayList<>();
            list.add((ObserverMouse) subscriber);
            subscribersSync.put(scene, list);
        }
        // notifyAll();
    }

    /**
     * Unsubscribe a new {@link ObserverKeyboard}
     *
     * @param subscriber the unsubscriber
     */
    @Override
    public synchronized void unsubscribe(Observer subscriber) {
//        while (iteratorSync) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                System.err.println("Thread Interrupted");
//            }
//        }
        for (Scene scene : subscribersSync.keySet())
            subscribersSync.get(scene).remove((ObserverMouse) subscriber);
        // notifyAll();
    }

    /**
     * Empty the subscriber list
     */
    @Override
    public synchronized void unsubscribeAll() {
//        while (iteratorSync) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                System.err.println("Thread Interrupted");
//            }
//        }
        subscribersSync.clear();
//        notifyAll();
    }

    /**
     * If the mouse is clicked
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        event = e;
        Thread th = new Thread(this::notifyAllSubs);
        th.start();
    }

    /**
     * If the mouse click is pressed
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * If the mouse click is released
     *
     * @param e the event to be processed
     */
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

    /**
     * If the mouse was moved
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        event = e;
        Thread th = new Thread(this::notifyAllSubs);
        th.start();
    }


}
