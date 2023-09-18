package casm.Scenes;

import casm.ECS.Components.MouseListener;
import casm.ECS.Components.TextComponent;
import casm.ECS.GameObject;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Factory.MenuFactory.MenuEntityFactory;
import casm.Factory.MenuFactory.MenuEntityType;
import casm.Game;
import casm.Objects.Menu.BackgroundImageObject;
import casm.Objects.Menu.Button;
import casm.Objects.Object;
import casm.Objects.TextObject;
import casm.Scenes.Level.LevelSaverLoader;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.StateMachine.AnimationStateMachine.State;
import casm.Utils.Renderer;
import casm.Utils.Vector2D;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The settings menu scene
 */
public class SettingsMenuScene extends Scene implements State {
    /**
     * The factory for creating the game objects
     */
    private final Factory factory;
    /**
     * The name of the scene
     */
    private final String name;
    /**
     * The list of buttons in the scene
     */
    private final ArrayList<Button> buttons = new ArrayList<>();

    /**
     * @param type The type of scene
     */
    public SettingsMenuScene(SceneType type) {
        super(type);
        factory = new MenuEntityFactory();
        name = "Settings Menu";
    }

    /**
     * The layers of the scene
     */
    public enum layers {
        BACKGROUND,
        FOREGROUND
    }

    /**
     * Construct the menu
     */
    private void menuConstruct() {
        Sprite buttonsBg = AssetsCollection.getInstance().getSprite("menu_assets\\settings_buttons_bg.png");

        createObject(MenuEntityType.BACKGROUND_IMAGE, new Vector2D(), layers.BACKGROUND);
        BackgroundImageObject titleBar = (BackgroundImageObject) createObject(MenuEntityType.TITLE_BAR, new Vector2D(), layers.BACKGROUND);
        BackgroundImageObject buttonsBG = (BackgroundImageObject) createObject(MenuEntityType.BACKGROUND_SETTINGS,
                new Vector2D(), layers.BACKGROUND);

        Vector2D buttonsPlace = buttonsBG.getSpawnPosition();
        buttons.add((Button) createObject(MenuEntityType.BUTTON.BACK, ((Vector2D) buttonsPlace.clone()).add(
                new Vector2D(buttonsBG.getWidth() - 90, buttonsBG.getHeight() - 75)), layers.FOREGROUND));
        TextObject text;
        //        TextObject text = new TextObject("Best Score", new Vector2D(), 25).center(buttonsPlace, buttonsBG.getWidth(), 110);
//        addGameObjectToScene(text);
//        addGameObjectToLayer(text, layers.FOREGROUND.ordinal());
        try {
            HashMap<Integer, Double> highScores = LevelSaverLoader.getInstance().getHighScore();
            for (int level : highScores.keySet()) {
                text = new TextObject("Level " + level + ": " + highScores.get(level), new Vector2D(
                        buttonsPlace.x + (double) buttonsBG.getWidth() / 4, buttonsPlace.y + 15 + level * 25), 20);
                addGameObjectToScene(text);
                addGameObjectToLayer(text, layers.FOREGROUND.ordinal());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //TODO: fa cumva mai frumoasa crearea de text
//        GameObject text = new GameObject("Text");
//        text.addComponent(new TextComponent("Settings", new Vector2D(0, 0), "Eras Bold ITC", 30,
//                new Color(219, 246, 255)));
//        Vector2D size = text.getComponent(TextComponent.class).getSize();
//        text.getComponent(TextComponent.class).setPosition(new Vector2D(titleBar.getSpawnPosition()).center(size.x, size.y,
//                titleBar.getWidth(), titleBar.getHeight() - size.y / 2));
        text = new TextObject("Best Score", new Vector2D(0, 0), 30)
                .center(titleBar.getSpawnPosition(), titleBar.getWidth(), titleBar.getHeight());
        addGameObjectToScene(text);
        addGameObjectToLayer(text, layers.FOREGROUND.ordinal());
    }

    /**
     * Create an entity for the scene
     *
     * @param type          The type of the object
     * @param spawnPosition The spawn position of the object
     * @param layers        The layer the object will be added to
     * @return The created object
     */
    private Object createObject(FactoryTypes type, Vector2D spawnPosition, layers layers) {
        Object entity = factory.create(type, spawnPosition);
        addGameObjectToScene(entity);
        addGameObjectToLayer(entity, layers.ordinal());
        return entity;
    }

    /**
     * Remove entity form the menu
     *
     * @param entity The entity to be removed
     */
    public void removeEntity(GameObject entity) {
        removeGameObject(entity);
    }

    @Override
    public void init() {
        menuConstruct();
        gameObjects.forEach(GameObject::init);
        isRunning = true;
    }

    @Override
    public void destroy() {
        Thread th = new Thread(() -> {
            buttons.forEach(it -> MouseListener.getInstance().unsubscribe(it));
            super.destroy();
            buttons.clear();
        });
        th.start();
    }

    /**
     * @return The name of the scene
     */
    @Override
    public String getName() {
        return null;
    }
}
