package casm.Scenes.Level;

import casm.ECS.Components.KeyboardControllerComponent;
import casm.ECS.Components.KeyboardListener;
import casm.ECS.GameObject;
import casm.Exception.SceneCanNotBeSaved;
import casm.Factory.EntityFactory.EntityFactory;
import casm.Factory.EntityFactory.EntityType;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Map.Map;
import casm.Objects.Entities.Enemies.Enemy;
import casm.Objects.Entities.Entity;
import casm.Objects.Entities.Player;
import casm.Objects.Entities.SpawnDoor;
import casm.Objects.Entities.WinDoor;
import casm.Objects.HeartBonus;
import casm.Objects.InfoBar;
import casm.Objects.Object;
import casm.Scenes.Scene;
import casm.Scenes.SceneType;
import casm.SpriteUtils.AssetsCollection;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.StateMachine.AnimationStateMachine.State;
import casm.Utils.Vector2D;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LeveleScene extends Scene implements State, Originator {

    private Player player;
    private WinDoor winDoor;
    private Set<Enemy> enemies = new HashSet<>();
    private Set<HeartBonus> heartBonuses = new HashSet<>();
    public InfoBar infoBar;
    private Factory factory;
    private boolean restored = false, restore = true, won = false;
    private String name;
    private int level = 1;
    private double score = 0;

    public LeveleScene(SceneType type) {
        super(type);
        factory = new EntityFactory();
        name = "LevelScene";
    }

    public LeveleScene(SceneType type, int level) {
        super(type);
        factory = new EntityFactory();
        name = "LevelScene";
        this.level = level;
    }

    public enum layers {
        BACKGROUND,
        ENTITY,
        FOREGROUND
    }

    private void levelConstruct() {

        long time = System.nanoTime();
        AssetsCollection.getInstance().addSpriteSheet("player_single_frame.png", 16, 22);
        infoBar = new InfoBar(this, layers.FOREGROUND.ordinal(), new Vector2D(5, 5));
        Map.loadMap(this, "level" + level + ".tmj", "level" + level + "_tiles.png");

        if (restore) {
            try {
                LevelSaverLoader.getInstance().load();
            } catch (SceneCanNotBeSaved | SQLException e) {
                throw new RuntimeException(e);
            }
            if (!restored)
                constructEntities(Map.getPlayerSpawnPosition(), Map.getEnemiesSpawnPosition(), Map.getHeartsSpawnPosition());
        } else
            constructEntities(Map.getPlayerSpawnPosition(), Map.getEnemiesSpawnPosition(), Map.getHeartsSpawnPosition());


        SpawnDoor spawnDoor = new SpawnDoor("Spawn Door", Map.getSpawnDoor());
        addGameObjectToScene(spawnDoor);
        addGameObjectToLayer(spawnDoor, layers.BACKGROUND.ordinal());
        winDoor = new WinDoor("Win Door", Map.getWinDoor());
        addGameObjectToScene(winDoor);
        addGameObjectToLayer(winDoor, layers.BACKGROUND.ordinal());

        System.out.println(System.nanoTime() - time);
    }

    private void constructEntities(Vector2D playerPosition, HashMap<EntityType, Set<Vector2D>> enemyPositions, Set<Vector2D> heartsPositions) {
        player = (Player) createEntity(EntityType.PLAYER, playerPosition);
        infoBar.updateHealth(player.getLife());
        infoBar.updateScore(score);
        for (EntityType type : enemyPositions.keySet()) {
            enemyPositions.get(type).forEach(position -> {
                // Runnable runnableEnemy = () -> {
                Enemy enemy = (Enemy) createEntity(type, position);
                //    synchronized (this) {
                enemies.add(enemy);
                //         System.out.println("Gata inammic");
                //     }
                //  };
                //  new Thread(runnableEnemy).start();
            });
        }
        heartsPositions.forEach(position -> heartBonuses.add((HeartBonus) createEntity(EntityType.HEART_BONUS, position)));
//        if (!enemyPosition.isEmpty()) {
//            Catfish enemy = (Catfish) createEntity(EntityType.CATFISH, (Vector2D) enemyPosition.toArray()[3]);
//            enemies.add(enemy);
//        }
    }

    private Object createEntity(FactoryTypes type, Vector2D spawnPosition) {
        Object entity = factory.create(type, spawnPosition);
        addGameObjectToScene(entity);
        addGameObjectToLayer(entity, layers.ENTITY.ordinal());
        return entity;
    }

    public void removeEntity(GameObject entity) {
        if (player == entity)
            player = null;
        else
            enemies.remove((Enemy) entity);
        removeGameObject(entity);
    }

    @Override
    public void checkForDeaths() {
        for (int i = 0; i < gameObjects.size(); ++i)
            if (!gameObjects.get(i).isAlive()) {
                if (player == gameObjects.get(i))
                    player = null;
                if(gameObjects.get(i) instanceof Enemy)
                    enemies.remove((Enemy) gameObjects.get(i));
                if(gameObjects.get(i) instanceof HeartBonus)
                    heartBonuses.remove((HeartBonus) gameObjects.get(i));
            }
        super.checkForDeaths();
        checkForLevelDone();
    }

    @Override
    public void init() {
        levelConstruct();
        gameObjects.forEach(GameObject::init);
        isRunning = true;
    }

    private void checkForLevelDone() {
        if (isRunning)
            if (enemies.isEmpty()) {
                if (!won) {
                    System.out.println("WIN!");
                    won = true;
                    if (winDoor != null)
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

    @Override
    public void destroy() {
        Thread th = new Thread(() -> {
            KeyboardListener.getInstance().unsubscribe(player.getComponent(KeyboardControllerComponent.class));
            super.destroy();
        });
        th.start();
    }

    @Override
    public String getName() {
        return null;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public Memento save() {
        return new LevelMemento(player, enemies, heartBonuses, level, score);
    }

    @Override
    public boolean restore(Memento memo) {
        //TODO: Fa lode u daca exista mai ok, nu asa
        if (memo != null) {
            LevelMemento memento = (LevelMemento) memo;
            //TODO: Daca gasesti o solutie mai buna
            if (memento.getLevel() >= level) {
                this.level = memento.getLevel();
                player = (Player) createEntity(EntityType.PLAYER, memento.getPlayer().getPosition());
                player.setLife(memento.getPlayer().getHealth());
                setScore(memento.getScore());
                memento.getEntities().forEach(savedEntity -> {
                    Object entity = createEntity(savedEntity.getType(), savedEntity.getPosition());
                    if(savedEntity.getType() == EntityType.HEART_BONUS)
                        heartBonuses.add((HeartBonus) entity);
                    else {
                        ((Enemy) entity).setLife(savedEntity.getHealth());
                        enemies.add(((Enemy) entity));
                    }
                });
                restored = true;
                return true;
            }
        }
        return false;
    }

    public double addToScore(double score) {
        this.score = score;
        infoBar.updateScore(score);
        return this.score;
    }

    public void resetScore() {
        this.score = 0;
        infoBar.updateScore(score);
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
        infoBar.updateScore(score);
    }

    public LeveleScene load(boolean restore) {
        this.restore = restore;
        return this;
    }

    public Set<HeartBonus> getHeartBonuses() {
        return heartBonuses;
    }
}
