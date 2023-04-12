package casm.ECS;

import casm.Game;
import casm.Utils.Renderer;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Scene {

    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected HashMap<Integer, List<GameObject>> layeringObjects = new HashMap<>();
    public int layerCounter = 0;

    public abstract void init();

    public void start() {
        gameObjects.forEach(GameObject::update);
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject obj) {
        if (!isRunning)
            gameObjects.add(obj);
        else {
            gameObjects.add(obj);
            obj.init();
        }
    }

    public void addGameOjectToLayer(GameObject obj, int layer) {
        if (!layeringObjects.containsKey(layer)) {
            layeringObjects.put(layer, new ArrayList<>());
            if(layerCounter < layer)
                layerCounter = layer;
        }
        layeringObjects.get(layer).add(obj);
    }

    public void update() {
        gameObjects.forEach(GameObject::update);
    }

    public void draw() {
        BufferStrategy bs = Game.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, Game.getWindow().GetWndWidth(), Game.getWindow().GetWndHeight());

        for (int i = 0; i <= layerCounter; i++) {
            if(layeringObjects.containsKey(i))
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
}
