package casm.Observer;

import casm.Scenes.Scene;

/**
 * The observable interface is the base interface for all the observables<br>
 * The observable is the object that the {@link Observer} can subscribe to,
 * and they will be notified when an event happens
 */
public interface Observable {

    /**
     * Subscribe to the observable to be notified when an event happens
     * @param scene the scene that the Observer is part of
     * @param subscriber the subscriber that will be notified
     */
    void subscribe(Scene scene, Observer subscriber);

    /**
     * Unsubscribe from the observable
     * @param subscriber the subscriber that will be unsubscribed
     */
    void unsubscribe(Observer subscriber);

    /**
     * Unsubscribe all the subscribers from the observable
     */
    void unsubscribeAll();

    /**
     * Notify all the subscribers that an event happened
     */
    void notifyAllSubs();
}
