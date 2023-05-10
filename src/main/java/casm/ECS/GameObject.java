package casm.ECS;

import casm.ECS.Components.KeyboardControllerComponent;
import casm.ECS.Components.KeyboardListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The game engine is based on Entity Component System(ECS)<br>
 * GameObject is the Entity class for the game<br>
 * You could add components to the entity, and they will be initialized, updated and drawn on the screen
 */
public class GameObject {

    /**
     * Counts the number of entities in the System, used for giving ids to new entities
     */
    private static int ENTITIES_COUNTER = 0;
    /**
     * The entity id, that is unique for every entity
     */
    private int id = -1;
    /**
     * The name of the entity
     */
    private String name;
    /**
     * If the entity is still alive or it was killed
     */
    private boolean alive = true;


    /**
     * List of components that are attributed to the entity and will be updated
     */
    private List<Component> components;

    /**
     * Create a new entity
     * @param name name for the entity
     */
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        id = ENTITIES_COUNTER++;
    }

    /**
     * Get a component of the entity
     * @param componentClass the component that you are searching for, it needs to be inherited from Component
     *                       and to be of class type
     * @return the component searched for or null if it is not found in the game object
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Castring component!";
                }
            }
        }
        return null;
    }

    /**
     * Get if the entity has a component
     * @param componentClass the component that you are searching for, it needs to be inherited from Component
     *                       and to be of class type
     * @return true if the entity has the searched component and false if it was not found
     */
    public <T extends Component> boolean hasComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass()))
                return true;
        }
        return false;
    }

    /**
     * Remove a component form the entity
     * @param componentClass the component that you are searching for removing, it needs to be inherited from Component
     *                       and to be of class type
     */
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        components.removeIf(component -> componentClass.isAssignableFrom(component.getClass()));
    }

    /**
     * Add a new component to the game object
     * The component that will be added needs to be inherited from Component
     * @param component the component that you want to add to the entity
     */
    public void addComponent(Component component) {
        this.components.add(component);
        component.gameObject = this;
    }

    /**
     * Kill the entity
     * The game engine will handler distorting the entity after that
     * @see casm.Scenes.Scene
     */
    public void kill() {
        alive = false;
    }

    /**
     * Destroy the entity
     * It is unsafe, if you want to destroy the entity use kill() and the game will destroy it in a safe manner in checkForDeaths()
     * @see casm.Scenes.Scene
     */
    public void destroy() {
        components.forEach(Component::destroy);
        components.clear();
        alive = false;
    }

    /**
     * Initializing every component of the object
     */
    public void init() {
        components.forEach(Component::init);
    }

    /**
     * Updating every component of the object
     */
    public void update() {
        components.forEach(Component::update);
    }

    /**
     * Drawing every component of the object
     */
    public void draw() {
        components.forEach(Component::draw);
    }

    /**
     * Handling every component of the object
     */
    public void eventHandler() {
        components.forEach(Component::eventHandler);
    }

    /**
     * Get the name of the entity
     * @return entity name
     */
    public String getName() {
        return name;
    }

    /**
     * Get entity id
     * @return entity id
     */
    public int getId() {
        return id;
    }

    /**
     * Get if the entity is still alive
     * @return if the entity is alive
     */
    public boolean isAlive() {
        return alive;
    }
}
