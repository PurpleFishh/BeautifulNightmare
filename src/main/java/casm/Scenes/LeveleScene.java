package casm.Scenes;

import casm.ECS.GameObject;
import casm.Entities.Enemies.Enemy;
import casm.Entities.Player;
import casm.Entities.Enemies.WeaselFisherman;
import casm.Map.Map;
import casm.SpriteUtils.AssetsCollection;
import casm.Utils.Vector2D;

import java.util.HashSet;
import java.util.Set;

public class LeveleScene extends Scene {

    private Player player;
    private Set<Enemy> enemies = new HashSet<>();

    public LeveleScene() {

    }

    public enum layers {
        BACKGROUND,
        ENTITY,
        FOREGROUND
    }

    private void levelConstruct() {
        AssetsCollection.addSpritesheet("player_single_frame.png", 16, 22);
        Map.loadMap(this, "level1.tmj", "tiles2.png");

        player = new Player(Map.getPlayerSpawnPosition());
        addGameObjectToScene(player);
        addGameObjectToLayer(player, layers.ENTITY.ordinal());
        Map.getEnemiesSpawnPosition().forEach(position -> {
            WeaselFisherman enemy = new WeaselFisherman(position);
            addGameObjectToScene(enemy);
            addGameObjectToLayer(enemy, layers.ENTITY.ordinal());
            enemies.add(enemy);
        });
    }
    public void removeEntity(GameObject entity)
    {
        if(player == entity)
            player = null;
        enemies.remove(entity);
        removeGameObject(entity);
    }
    @Override
    public void checkForDeaths(){
        for(int i = 0; i < gameObjects.size(); ++i)
            if(!gameObjects.get(i).isAlive()) {
                if(player == gameObjects.get(i))
                    player = null;
                enemies.remove(gameObjects.get(i));
            }
        super.checkForDeaths();
    }

    @Override
    public void init() {
        levelConstruct();
        gameObjects.forEach(GameObject::init);
    }

    public Player getPlayer() {
        return player;
    }

    public Set<Enemy> getEnemies() {
        return enemies;
    }
}
