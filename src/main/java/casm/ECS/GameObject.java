package casm.ECS;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private static int ENTITIES_COUNTER = 0;
    private int id = -1;
    private String name;
    private boolean alive = true;


    private List<Component> components;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        id = ENTITIES_COUNTER++;
    }

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
    public <T extends Component> boolean hasComponent(Class<T> componentClass)
    {
        for (Component c : components) {
            if(componentClass.isAssignableFrom(c.getClass()))
                return true;
        }
        return false;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        components.removeIf(component -> componentClass.isAssignableFrom(component.getClass()));
    }

    public void addComponent(Component component) {
        this.components.add(component);
        component.gameObject = this;
    }

    public void destroy() {
        components.forEach(Component::destroy);
        components.clear();
        alive = false;
    }

    public void init() {
        components.forEach(Component::init);
    }

    public void update() {
        components.forEach(Component::update);
    }

    public void draw() {
        components.forEach(Component::draw);
    }

    public void eventHandler() {
        components.forEach(Component::eventHandler);
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public boolean isAlive() {
        return alive;
    }
}
