package casm.ECS.Components;

import casm.Game;
import casm.Observer.Observable;
import casm.Observer.Observer;
import casm.Observer.ObserverKeyboard;
import casm.Scenes.Scene;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An observer that listens to all the keyboard events
 */
public class KeyboardListener implements Observable, KeyListener {
    /**
     * The subscribers of the observer
     */
    private final HashMap<Scene, List<ObserverKeyboard>> subscribersSync = new HashMap<>();
    /**
     * Used for synchronizing the access to the list
     */
    // private final List<ObserverKeyboard> subscribersSync = Collections.synchronizedList(subscribers);
    /**
     * The event that needs to be sent to the {@link ObserverKeyboard}
     */
    private KeyEvent event;
    /**
     * The instance of the singleton
     */
    private static KeyboardListener instance;
    /**
     * Used for synchronizing the access to the list(a semaphore in the access thread)
     */
    private final Boolean iteratorSync = false;

    private KeyboardListener() {
    }

    /**
     * @return get the instance of the singleton
     */
    public static KeyboardListener getInstance() {
        if (instance == null) instance = new KeyboardListener();
        return instance;
    }

    /**
     * Notify all the subscribers of the event that happened
     */
    @Override
    public synchronized void notifyAllSubs() {
        //iteratorSync = true;
        if (Game.getCurrentScene().isEmpty())
            return;
        if (subscribersSync.containsKey(Game.getCurrentScene().get())) {
            List<ObserverKeyboard> copyList = new ArrayList<>(subscribersSync.get(Game.getCurrentScene().get()));
            for (ObserverKeyboard observerKeyboard : copyList) observerKeyboard.notify(event);
        }
        //iteratorSync = false;
        // notifyAll();
    }

    /**
     * Subscribe a new {@link ObserverKeyboard}
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
            subscribersSync.get(scene).add((ObserverKeyboard) subscriber);
        else {
            ArrayList<ObserverKeyboard> list = new ArrayList<>();
            list.add((ObserverKeyboard) subscriber);
            subscribersSync.put(scene, list);
        }
        //notifyAll();
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
            subscribersSync.get(scene).remove((ObserverKeyboard) subscriber);
        //notifyAll();
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
        // notifyAll();
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * If a key is pressed event
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        event = e;
        Thread th = new Thread(this::notifyAllSubs);
        th.start();
    }

    /**
     * If a key is released event
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        event = e;
        Thread th = new Thread(this::notifyAllSubs);
        th.start();
    }
}
