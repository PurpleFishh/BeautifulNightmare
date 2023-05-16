package casm.Observer;

import casm.Scenes.Scene;

public interface Observable {

    void subscribe(Scene scene, Observer subscriber);
    void unsubscribe(Observer subscriber);
    void unsubscribeAll();
    void notifyAllSubs();
}
