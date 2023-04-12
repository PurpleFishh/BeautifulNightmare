package casm.ECS;

public abstract class Component {
    public GameObject gameObject = null;

    public void init() { }
    public void update() { };
    public void draw() { };
    public void eventHandler() { };
    public void destroy() { };

}
