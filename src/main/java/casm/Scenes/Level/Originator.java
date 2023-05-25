package casm.Scenes.Level;

/**
 * The scene that the memento is done for, or the scene that restores the memento
 */
public interface Originator {
    /**
     * Saves the state of the scene
     * @return The memento of the scene
     */
    Memento save();

    /**
     * Restores the state of the scene
     * @param memo The memento to restore the scene to
     * @return If the scene was restored
     */
    boolean restore(Memento memo);
}
