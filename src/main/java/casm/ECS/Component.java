package casm.ECS;

/**
 * Component is an abstract class used to creat Components for game objects
 */
public abstract class Component {
    /**
     * The game object that has the component attributed
     */
    public GameObject gameObject = null;

    /**
     * Called when all the components are initialized(it is not the constructor)
     */
    public void init() { }

    /**
     * Called on every iteration of the game loop for entities to be updated
     */
    public void update() { };

    /**
     * Called on every iteration of the game loop, after the entities are update, they will be drawn
     */
    public void draw() { };

    /**
     * Called on every iteration of the game loop for handling events like keyboard usage
     */
    public void eventHandler() { };

    /**
     * Used to kill end destroy entities
     * It will deallocate every component and after delete the object
     * It will no more be part of the scene or game
     */
    public void destroy() { };

}
