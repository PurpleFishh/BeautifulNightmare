package casm.Exception;

/**
 * Exception thrown when the scene can not be saved by the {@link casm.Scenes.Level.LevelSaverLoader}
 */
public class SceneCanNotBeSaved extends Exception{

    public SceneCanNotBeSaved()
    {
        super("This scene can not be saved!");
    }
}
