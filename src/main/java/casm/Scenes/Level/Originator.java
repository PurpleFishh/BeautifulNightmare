package casm.Scenes.Level;

public interface Originator {
    Memento save();
    boolean restore(Memento memo);
}
