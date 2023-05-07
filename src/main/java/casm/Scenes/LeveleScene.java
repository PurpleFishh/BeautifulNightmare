package casm.Scenes;

import casm.ECS.GameObject;
import casm.Entities.*;
import casm.Entities.Enemies.Enemy;
import casm.Entities.Enemies.WeaselFisherman;
import casm.Factory.EntityFactory.EntityFactory;
import casm.Factory.EntityFactory.EntityType;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Map.Map;
import casm.SpriteUtils.AssetsCollection;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.Utils.Vector2D;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class LeveleScene extends Scene {

    private Player player;
    private WinDoor winDoor;
    private Set<Enemy> enemies = new HashSet<>();
    private Factory factory;
    private boolean won = false;

    public LeveleScene() {
        factory = new EntityFactory();
    }

    public enum layers {
        BACKGROUND,
        ENTITY,
        FOREGROUND
    }

    private void levelConstruct() {
        AssetsCollection.getInstance().addSpriteSheet("player_single_frame.png", 16, 22);
        Map.loadMap(this, "level1.tmj", "tiles2.png");

        player = (Player) createEntity(EntityType.PLAYER, Map.getPlayerSpawnPosition());

        Map.getEnemiesSpawnPosition().forEach(position -> {
            WeaselFisherman enemy = (WeaselFisherman) createEntity(EntityType.WEASEL_FISHERMAN, position);
            enemies.add(enemy);
        });

        SpawnDoor spawnDoor = new SpawnDoor("Spawn Door", Map.getSpawnDoor());
        addGameObjectToScene(spawnDoor);
        addGameObjectToLayer(spawnDoor, layers.BACKGROUND.ordinal());
        winDoor = new WinDoor("Win Door", Map.getWinDoor());
        addGameObjectToScene(winDoor);
        addGameObjectToLayer(winDoor, layers.BACKGROUND.ordinal());
    }

    private Entity createEntity(FactoryTypes type, Vector2D spawnPosition) {
        Entity entity = factory.create(type, spawnPosition);
        addGameObjectToScene(entity);
        addGameObjectToLayer(entity, layers.ENTITY.ordinal());
        return entity;
    }

    public void removeEntity(GameObject entity) {
        if (player == entity)
            player = null;
        enemies.remove(entity);
        removeGameObject(entity);
    }

    @Override
    public void checkForDeaths() {
        for (int i = 0; i < gameObjects.size(); ++i)
            if (!gameObjects.get(i).isAlive()) {
                if (player == gameObjects.get(i))
                    player = null;
                enemies.remove(gameObjects.get(i));
            }
        super.checkForDeaths();
        checkForLevelDone();
    }

    @Override
    public void init() {
        levelConstruct();
        gameObjects.forEach(GameObject::init);
    }

    private void checkForLevelDone() {
        if (enemies.isEmpty()) {
            if (!won) {
                System.out.println("WIN!");
                won = true;
                winDoor.getComponent(AnimationStateMachine.class).trigger("open");
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Set<Enemy> getEnemies() {
        return enemies;
    }

    public boolean isWon() {
        return won;
    }

    public WinDoor getWinDoor() {
        return winDoor;
    }
}
