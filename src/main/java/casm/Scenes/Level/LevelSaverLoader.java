package casm.Scenes.Level;

import casm.Exception.SceneCanNotBeSaved;
import casm.Factory.EntityFactory.EntityType;
import casm.Game;
import casm.Objects.Entities.Enemies.Enemy;
import casm.Objects.Entities.Entity;
import casm.Objects.Entities.Player;
import casm.Utils.Vector2D;

import java.sql.*;
import java.util.*;

/**
 * Manages the saving and loading of the game
 */
public class LevelSaverLoader {

    private static LevelSaverLoader instance;

    private LevelSaverLoader() {
    }

    /**
     * @return The instance of the class
     */
    public static LevelSaverLoader getInstance() {
        if (instance == null)
            instance = new LevelSaverLoader();
        return instance;
    }

    /**
     * The history of the game, the most recent memento is at the top of the stack
     */
    private Stack<LevelMemento> history = new Stack<>();
    /**
     * Used in database to save the memento to it
     */
    private final String insertAntet = "INSERT INTO save_entities (TYPE, POSITION_X, POSITION_Y, HEALTH)";


    /**
     * Saves the current scene to the history and to the database
     *
     * @throws SceneCanNotBeSaved If the scene can not be saved(the current scenes are not a savable scene)
     */
    public void save() throws SceneCanNotBeSaved {
        if (Game.getLevelScene() == null)
            throw new SceneCanNotBeSaved();
        history.push((LevelMemento) ((LeveleScene) Game.getLevelScene()).save());
        try {
            saveToDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restores the scene from the database
     *
     * @throws SceneCanNotBeSaved If the scene can not be saved(the current scenes are not a savable scene)
     * @throws SQLException       If the database can not be accessed
     */
    public void load() throws SceneCanNotBeSaved, SQLException {
        if (Game.getLevelScene() == null)
            throw new SceneCanNotBeSaved();
        LevelMemento memento = restoreFromDataBase();
        if (memento != null) {
            history.push(restoreFromDataBase());
        }
        (Game.getLevelScene()).restore(memento);
    }

    /**
     * Save the memento to database
     *
     * @throws SQLException If the database can not be accessed
     */
    public void saveToDataBase() throws SQLException {
        Connection conn = getConnection();
        Statement stam = conn.createStatement();
        conn.setAutoCommit(false);

        stam.execute("CREATE TABLE IF NOT EXISTS save_entities(ID INTEGER PRIMARY KEY AUTOINCREMENT, TYPE TEXT, POSITION_X INT, POSITION_Y INT, HEALTH REAL);");
        conn.commit();

        stam.executeUpdate("DELETE FROM save_entities;");
        stam.executeUpdate("DELETE FROM sqlite_sequence WHERE name='save_entities';");
        conn.commit();

        LevelMemento mem = history.peek();
        saveEntity(stam, mem.getPlayer().getType(), mem.getPlayer());
        mem.getEntities().forEach(enemy -> {
            try {
                saveEntity(stam, enemy.getType(), enemy);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        conn.commit();

        stam.execute("CREATE TABLE IF NOT EXISTS save_info(LEVEL INTEGE, SCORE INT);");
        conn.commit();

        stam.executeUpdate("DELETE FROM save_info;");
        conn.commit();

        stam.executeUpdate("INSERT INTO save_info(LEVEL, SCORE) VALUES (" + mem.getLevel() + ", " + mem.getScore() + ");");
        conn.commit();

        stam.close();
        conn.close();
    }

    /**
     * Save the best score to the database
     *
     * @throws SQLException If the database can not be accessed
     */
    public void saveHighScore() throws SQLException {
        Connection conn = getConnection();
        Statement stam = conn.createStatement();
        conn.setAutoCommit(false);

        stam.execute("CREATE TABLE IF NOT EXISTS high_score(LEVEL INT PRIMARY KEY, HIGH_SCORE REAL);");
        conn.commit();

        ResultSet result = stam.executeQuery("SELECT HIGH_SCORE FROM high_score");
        double highScore = Objects.requireNonNull(Game.getLevelScene()).getScore();
        if (result.next())
            if (result.getDouble(1) > highScore)
                highScore = result.getDouble(1);

        stam.executeUpdate("INSERT OR REPLACE INTO high_score(LEVEL, HIGH_SCORE) VALUES (" + Game.getLevelScene().getLevel() + ", " + highScore + ");");
        conn.commit();

        stam.close();
        conn.close();
    }

    /**
     * Read the last memento from the database
     *
     * @return the read memento from the database
     * @throws SQLException If the database can not be accessed
     */
    public LevelMemento restoreFromDataBase() throws SQLException {
        Connection conn = getConnection();
        Statement stam = conn.createStatement();

        Set<LevelMemento.SavedEntity> savedEntitySet = new HashSet<>();

        ResultSet result = stam.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='save_entities';");
        if (!result.next())
            return null;
        result = stam.executeQuery("SELECT * FROM save_entities;");
        while ((result.next())) {
            EntityType type = EntityType.valueOf(result.getString(2));
            Vector2D position = new Vector2D(result.getDouble(3), result.getDouble(4));
            double health = result.getDouble(5);
            savedEntitySet.add(new LevelMemento.SavedEntity(type, position, health));
        }
        result = stam.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='save_info';");
        if (!result.next())
            return null;
        result = stam.executeQuery("SELECT * FROM save_info;");
        int level = result.getInt(1);
        double score = result.getDouble(2);
        stam.close();
        conn.close();

        return new LevelMemento(savedEntitySet, level, score);
    }

    public int getSavedLevelIndex() throws SQLException {
        Connection conn = getConnection();
        Statement stam = conn.createStatement();

        ResultSet result = stam.executeQuery("SELECT name FROM  sqlite_master  WHERE type='table' AND name='save_info';");
        int level = 1;
        if(result.next()) {
            result = stam.executeQuery("SELECT LEVEL FROM save_info");
            if (result.next())
                level = result.getInt(1);
            stam.close();
            conn.close();
        }
        return level;
    }

    /**
     * Save an entity state to the database
     *
     * @param stam   The statement to execute the query
     * @param type   The type of the entity
     * @param entity The entity to save
     * @throws SQLException If the database can not be accessed
     */
    private void saveEntity(Statement stam, EntityType type, LevelMemento.SavedEntity entity) throws SQLException {
        stam.executeUpdate(insertAntet + " VALUES ('" + type.name() + "', " +
                entity.getPosition().x + ", " + entity.getPosition().y + ", " + entity.getHealth() + ")");
    }

    /**
     * Read the high score from the database
     *
     * @return The high score
     * @throws SQLException If the database can not be accessed
     */
    public HashMap<Integer, Double> getHighScore() throws SQLException {
        Connection conn = getConnection();
        Statement stam = conn.createStatement();
        conn.setAutoCommit(false);

        HashMap<Integer, Double> highScores = new HashMap<>();
        ResultSet result = stam.executeQuery("SELECT * FROM high_score");
        while (result.next()) {
            highScores.put(result.getInt(1), result.getDouble(2));
        }

        stam.close();
        conn.close();
        return highScores;
    }

    /**
     * @return A connection to the database
     */
    public Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + "level_save.sqlite");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return c;
    }
}
