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

public class LevelSaverLoader {

    private static LevelSaverLoader instance;

    private LevelSaverLoader() {
    }

    public static LevelSaverLoader getInstance() {
        if (instance == null)
            instance = new LevelSaverLoader();
        return instance;
    }

    private Stack<LevelMemento> history = new Stack<>();
    private final String insertAntet = "INSERT INTO save_entities (TYPE, POSITION_X, POSITION_Y, HEALTH)";


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

    public void load() throws SceneCanNotBeSaved, SQLException {
        if (Game.getLevelScene() == null)
            throw new SceneCanNotBeSaved();
        LevelMemento memento = restoreFromDataBase();
        if (memento != null) {
            history.push(restoreFromDataBase());
        }
        (Game.getLevelScene()).restore(memento);
    }

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

    private void saveEntity(Statement stam, EntityType type, LevelMemento.SavedEntity entity) throws SQLException {
        stam.executeUpdate(insertAntet + " VALUES ('" + type.name() + "', " +
                entity.getPosition().x + ", " + entity.getPosition().y + ", " + entity.getHealth() + ")");
    }

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
