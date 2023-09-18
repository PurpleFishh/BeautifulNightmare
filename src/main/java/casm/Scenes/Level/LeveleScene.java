package casm.Scenes.Level;

import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.Rectangle;
import casm.ECS.Components.KeyboardControllerComponent;
import casm.ECS.Components.KeyboardListener;
import casm.ECS.Components.PositionComponent;
import casm.ECS.GameObject;
import casm.Exception.SceneCanNotBeSaved;
import casm.Factory.EntityFactory.EntityFactory;
import casm.Factory.EntityFactory.EntityType;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Map.Map;
import casm.Objects.Entities.Enemies.Enemy;
import casm.Objects.Entities.Player;
import casm.Objects.Entities.SpawnDoor;
import casm.Objects.Entities.WinDoor;
import casm.Objects.HeartBonus;
import casm.Objects.InfoBar;
import casm.Objects.Menu.BackgroundImageObject;
import casm.Objects.Object;
import casm.Scenes.Scene;
import casm.Scenes.SceneType;
import casm.SpriteUtils.AssetsCollection;
import casm.StateMachine.AnimationStateMachine.AnimationStateMachine;
import casm.StateMachine.AnimationStateMachine.State;
import casm.Utils.Settings.EntitiesSettings;
import casm.Utils.Settings.Setting;
import casm.Utils.Vector2D;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * LevelScene is the scene that contains a level of the game.
 */
public class LeveleScene extends Scene implements State, Originator {

    /**
     * The player.
     */
    private Player player;
    /**
     * The door that the player must reach to win the level.
     */
    private WinDoor winDoor;
    /**
     * List of colliders where if the player hits them he wins the level
     */
    private final ArrayList<Rectangle> winColliders = new ArrayList<>();
    /**
     * List of the enemies.
     */
    private final Set<Enemy> enemies = new HashSet<>();
    /**
     * List of the heart bonuses.
     */
    private final Set<HeartBonus> heartBonuses = new HashSet<>();
    /**
     * Info bar to display player health and score.
     */
    public InfoBar infoBar;
    /**
     * The factory used to create entities.
     */
    private final Factory factory;
    /**
     * <b>restored</b> is true if the scene was restored from the database, false otherwise.<br>
     * <b>restore</b> is true if the scene must be restored from the database, false otherwise.<br>
     * <b>won</b> is true if the player won the level, and now the door is opened, false otherwise.
     */
    private boolean restored = false, restore = true, won = false;
    /**
     * The name of the scene.
     */
    private final String name;
    /**
     * The level that the scene contains.
     */
    private int level = 1;
    /**
     * The score of the player.
     */
    private double score = 0;

    /**
     * @param type The type of the scene.
     */
    public LeveleScene(SceneType type) {
        super(type);
        factory = new EntityFactory();
        name = "LevelScene";
    }

    /**
     * Crate a new level scene of the specified level.
     *
     * @param type  The type of the scene.
     * @param level The level that the scene contains.
     */
    public LeveleScene(SceneType type, int level) {
        super(type);
        factory = new EntityFactory();
        name = "LevelScene";
        this.level = level;
    }

    /**
     * Scene layers.
     */
    public enum layers {
        BACKGROUND,
        ENTITY,
        FOREGROUND
    }

    /**
     * Construct the level.
     */
    private void levelConstruct() {

        long time = System.nanoTime();
        AssetsCollection.getInstance().addSpriteSheet("player_single_frame.png", 16, 22);
        infoBar = new InfoBar(this, layers.FOREGROUND.ordinal(), new Vector2D(5, 5));
        if (level == 1) {
            BackgroundImageObject levelBg = new BackgroundImageObject(new Vector2D(0, 0),
                    AssetsCollection.getInstance().addSprite("level1_bg.png"), Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT);
            addGameObjectToScene(levelBg);
            addGameObjectToLayer(levelBg, layers.BACKGROUND.ordinal());
        }
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

        if (Map.getSpawnDoor() != null) {
            SpawnDoor spawnDoor = new SpawnDoor("Spawn Door", Map.getSpawnDoor());
            addGameObjectToScene(spawnDoor);
            addGameObjectToLayer(spawnDoor, layers.BACKGROUND.ordinal());
        }
        if (Map.getWinDoor() != null) {
            winDoor = new WinDoor("Win Door", Map.getWinDoor());
            addGameObjectToScene(winDoor);
            addGameObjectToLayer(winDoor, layers.BACKGROUND.ordinal());
            winColliders.add(winDoor.getComponent(ColliderComponent.class).getCollider(ColliderType.WIN_DOOR));
        }

        if (level == 1) {
            player.getComponent(PositionComponent.class).setMaxJump(EntitiesSettings.PlayerInfo.PLAYER_MAX_JUMP_PARKOUR);
        }

        System.out.println(System.nanoTime() - time);
    }

    /**
     * Construct the needed entities.
     *
     * @param playerPosition  The position of the player.
     * @param enemyPositions  The positions of the enemies.
     * @param heartsPositions The positions of the heart bonuses.
     */
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
//            if (!enemyPositions.get(type).isEmpty()) {
//                Enemy enemy = (Enemy) createEntity(EntityType.CATFISH, (Vector2D) enemyPositions.get(type).toArray()[0]);
//                enemies.add(enemy);
//                break;
//            }
        }
        heartsPositions.forEach(position -> heartBonuses.add((HeartBonus) createEntity(EntityType.HEART_BONUS, position)));

    }

    /**
     * Create an entity.
     *
     * @param type          The type of the entity.
     * @param spawnPosition The position of the entity.
     * @return The created entity.
     */
    private Object createEntity(FactoryTypes type, Vector2D spawnPosition) {
        Object entity = factory.create(type, spawnPosition);
        addGameObjectToScene(entity);
        addGameObjectToLayer(entity, layers.ENTITY.ordinal());
        return entity;
    }

    /**
     * Remove an entity from the scene.
     *
     * @param entity The entity to be removed.
     */
    public void removeEntity(GameObject entity) {
        if (player == entity)
            player = null;
        else
            enemies.remove((Enemy) entity);
        removeGameObject(entity);
    }

    /**
     * Check for death objects.
     */
    @Override
    public void checkForDeaths() {
        for (int i = 0; i < gameObjects.size(); ++i)
            if (!gameObjects.get(i).isAlive()) {
                if (player == gameObjects.get(i))
                    player = null;
                if (gameObjects.get(i) instanceof Enemy)
                    enemies.remove((Enemy) gameObjects.get(i));
                if (gameObjects.get(i) instanceof HeartBonus)
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

    /**
     * Check if all the enemies are dead, that means that the level is complete.
     */
    private void checkForLevelDone() {
        if (isRunning)
            if (enemies.isEmpty()) {
                if (!won) {
                    won = true;
                    if (winDoor != null)
                        winDoor.getComponent(AnimationStateMachine.class).trigger("open");
                }
            }
    }

    /**
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return The enemies.
     */
    public Set<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * @return if the level is won.
     */
    public boolean isWon() {
        return won;
    }

    /**
     * @return the win door.
     */
    public WinDoor getWinDoor() {
        return winDoor;
    }

    /**
     * @return the colliders for winning the level.
     */
    public ArrayList<Rectangle> getWinColliders() {
        return winColliders;
    }

    /**
     * Destroy the level.
     */
    @Override
    public void destroy() {
        Thread th = new Thread(() -> {
            KeyboardListener.getInstance().unsubscribe(player.getComponent(KeyboardControllerComponent.class));
            super.destroy();
        });
        th.start();
    }

    /**
     * @return The name of the level.
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * @return The level id
     */
    public int getLevel() {
        return level;
    }

    /**
     * Save the level state into a memento.
     *
     * @return memento of the level
     */
    @Override
    public Memento save() {
        return new LevelMemento(player, enemies, heartBonuses, level, score);
    }

    /**
     * Restore the level state from a memento.
     *
     * @param memo The memento to restore the scene to the state it was when the memento was created.
     * @return true if the restore was successful, false otherwise.
     */
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
                    if (savedEntity.getType() == EntityType.HEART_BONUS)
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

    /**
     * @param score The score to be added to the current score.
     * @return The new score.
     */
    public double addToScore(double score) {
        this.score += score;
        infoBar.updateScore(score);
        return this.score;
    }

    /**
     * Reset the score to 0.
     */
    public void resetScore() {
        this.score = 0;
        infoBar.updateScore(score);
    }

    /**
     * @return The current score.
     */
    public double getScore() {
        return score;
    }

    /**
     * Set the score to a new value.
     *
     * @param score The new score.
     */
    public void setScore(double score) {
        this.score = score;
        infoBar.updateScore(score);
    }

    /**
     * Set if you want to load the level
     *
     * @param restore set if you want the level to be loaded from the database or not.
     * @return Reference to the level.
     */
    public LeveleScene load(boolean restore) {
        this.restore = restore;
        return this;
    }

    /**
     * @return set of heart bonuses.
     */
    public Set<HeartBonus> getHeartBonuses() {
        return heartBonuses;
    }

    /**
     * Add a collider to the win colliders.
     *
     * @param collider The collider to be added to the win colliders.
     */
    public void addToWinColliders(Rectangle collider) {
        winColliders.add(collider);
    }
}
