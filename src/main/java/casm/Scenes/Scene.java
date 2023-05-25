package casm.Scenes;

import casm.ECS.GameObject;
import casm.Game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The base for greating a scene for the game
 */
public abstract class Scene {

    /**
     * The type of scene
     */
    private SceneType type;
    /**
     * If the scene is running
     */
    protected boolean isRunning = false;
    /**
     * The list of game objects in the scene
     */
    protected List<GameObject> gameObjects = new ArrayList<>();
    /**
     * The list of game objects in the scene that are layered
     */
    protected HashMap<Integer, List<GameObject>> layeringObjects = new HashMap<>();
    /**
     * Counts the highest layer index used
     */
    public int layerCounter = 0;

    /**
     * @param type The type of scene
     */
    public Scene(SceneType type) {
        this.type = type;
    }

    /**
     * Initialize all the game objects in the scene
     */
    public abstract void init();

    /**
     * Destroy all the game objects and the scene
     */
    public void destroy() {
        gameObjects.forEach(GameObject::destroy);
        isRunning = false;
    }

    /**
     * Add a game object to the scene
     *
     * @param obj The game object to add to the scene
     */
    public void addGameObjectToScene(GameObject obj) {
        if (!isRunning)
            gameObjects.add(obj);
        else {
            gameObjects.add(obj);
            obj.init();
        }
    }

    /**
     * Add a game object to the scene layer
     *
     * @param obj   The game object to add to the scene layer
     * @param layer The layer to add the game object to
     */
    public void addGameObjectToLayer(GameObject obj, int layer) {
        if (!layeringObjects.containsKey(layer)) {
            layeringObjects.put(layer, new ArrayList<>());
            if (layerCounter < layer)
                layerCounter = layer;
        }
        layeringObjects.get(layer).add(obj);
    }

    /**
     * Remove a game object from the scene
     * @param gameObject The game object to remove from the scene
     */
    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
        layeringObjects.values().forEach(it -> it.remove(gameObject));
    }

    /**
     * Update all the game objects in the scene
     */
    public synchronized void update() {
        List<GameObject> copy = new ArrayList<>(gameObjects);
        copy.forEach(it -> {
            if (it.isAlive()) it.update();
        });
    }

    /**
     * Draw all the game objects in the scene
     */
    public void draw() {
        BufferStrategy bs = Game.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, Game.getWindow().GetWndWidth(), Game.getWindow().GetWndHeight());

        for (int i = 0; i <= layerCounter; i++) {
            if (layeringObjects.containsKey(i))
                layeringObjects.get(i).forEach(GameObject::draw);
        }

        //gameObjects.forEach(GameObject::draw);

        bs.show();
        g.dispose();
    }

    /**
     * @return The list of game objects in the scene
     */
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * @return The list of game objects in the scene that are layered
     */
    public HashMap<Integer, List<GameObject>> getLayeringObjects() {
        return layeringObjects;
    }

    /**
     * Check if any game objects in the scene are dead and remove them
     */
    public void checkForDeaths() {
        for (int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            if (!gameObject.isAlive()) {
                removeGameObject(gameObject);
                gameObject.destroy();
            }
        }
    }

    /**
     * @return If the scene is running
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * @return The type of scene
     */
    public SceneType getType() {
        return type;
    }
}
