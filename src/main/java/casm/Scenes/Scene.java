package casm.Scenes;

import casm.ECS.GameObject;
import casm.Game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Scene {

    private SceneType type;
    protected boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected HashMap<Integer, List<GameObject>> layeringObjects = new HashMap<>();
    public int layerCounter = 0;

    public Scene(SceneType type)
    {
        this.type = type;
    }

    public abstract void init();

    public void destroy() {
        gameObjects.forEach(GameObject::destroy);
        isRunning = false;
    }

    public void addGameObjectToScene(GameObject obj) {
        if (!isRunning)
            gameObjects.add(obj);
        else {
            gameObjects.add(obj);
            obj.init();
        }
    }

    public void addGameObjectToLayer(GameObject obj, int layer) {
        if (!layeringObjects.containsKey(layer)) {
            layeringObjects.put(layer, new ArrayList<>());
            if (layerCounter < layer)
                layerCounter = layer;
        }
        layeringObjects.get(layer).add(obj);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
        layeringObjects.values().forEach(it -> it.remove(gameObject));
    }

    public synchronized void update() {
        List<GameObject> copy = new ArrayList<>(gameObjects);
        copy.forEach(it -> {
            if (it.isAlive()) it.update();
        });
    }

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

    public void eventHandler() {
        gameObjects.forEach(GameObject::eventHandler);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public HashMap<Integer, List<GameObject>> getLayeringObjects() {
        return layeringObjects;
    }

    public void checkForDeaths()
    {
        for(int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            if (!gameObject.isAlive()) {
                removeGameObject(gameObject);
                gameObject.destroy();
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public SceneType getType() {
        return type;
    }
}
