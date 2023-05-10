package casm.Observer;

public interface Observable {

    void subscribe(Observer subscriber);
    void unsubscribe(Observer subscriber);
    void unsubscribeAll();
    void notifyAllSubs();
}
