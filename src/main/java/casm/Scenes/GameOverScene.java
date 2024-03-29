package casm.Scenes;

import casm.ECS.Components.MouseListener;
import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.TextComponent;
import casm.ECS.GameObject;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Factory.MenuFactory.MenuEntityFactory;
import casm.Factory.MenuFactory.MenuEntityType;
import casm.Objects.Menu.BackgroundImageObject;
import casm.Objects.Menu.Button;
import casm.Objects.Object;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.StateMachine.AnimationStateMachine.State;
import casm.Utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * The game over menu scene
 */
public class GameOverScene extends Scene implements State {
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
    public GameOverScene(SceneType type) {
        super(type);
        factory = new MenuEntityFactory();
        name = "GameOver Menu";
    }

    /**
     * The layers of the scene
     */
    public enum layers {
        BACKGROUND,
        FOREGROUND
    }

    /**
     * Construct the scene
     */
    private void menuConstruct() {
        loadAssets();
        Sprite buttonsBg = AssetsCollection.getInstance().addSprite("menu_assets\\menu_buttons_bg.png");

        createObject(MenuEntityType.BACKGROUND_IMAGE, new Vector2D(), layers.BACKGROUND);
        BackgroundImageObject titleBar = (BackgroundImageObject) createObject(MenuEntityType.TITLE_BAR, new Vector2D(), layers.BACKGROUND);
        BackgroundImageObject backButtons = (BackgroundImageObject) createObject(MenuEntityType.BACKGROUND_BUTTONS, new Vector2D(), layers.BACKGROUND);

        //TODO: fa cumva mai frumoasa crearea de text
        GameObject text = new GameObject("Text");
        text.addComponent(new TextComponent("GameOver", new Vector2D(0, 0), "Eras Bold ITC", 30,
                new Color(219, 246, 255)));
        Vector2D size = text.getComponent(TextComponent.class).getSize();
        text.getComponent(TextComponent.class).setPosition(new Vector2D(titleBar.getSpawnPosition()).center(size.x, size.y,
                titleBar.getWidth(), titleBar.getHeight() - size.y / 2));
        addGameObjectToScene(text);
        addGameObjectToLayer(text, layers.FOREGROUND.ordinal());

        Vector2D buttonsPlace = backButtons.getSpawnPosition();

        Button button = (Button) createObject(MenuEntityType.BUTTON.RETRY, buttonsPlace, layers.FOREGROUND);
        button.getComponent(PositionComponent.class).position = ((Vector2D) buttonsPlace.clone()).center(
                button.getWidth(), button.getHeight(), buttonsBg.getWidth() / 2, buttonsBg.getHeight());
        buttons.add(button);
        button = (Button) createObject(MenuEntityType.BUTTON.EXIT, buttonsPlace, layers.FOREGROUND);
        button.getComponent(PositionComponent.class).position = ((Vector2D) buttonsPlace.clone()).center(
                button.getWidth(), button.getHeight(), (int) (buttonsBg.getWidth() * 1.5), buttonsBg.getHeight());
        buttons.add(button);
    }

    /**
     * Load the assets for the scene
     */
    private void loadAssets() {
        AssetsCollection.getInstance().addSpriteSheet("menu_assets\\buttons\\buttons_spritesheet.png", 74, 58);
        AssetsCollection.getInstance().addSpriteSheet("menu_assets\\buttons\\back_button_spritesheet.png", 42, 42);
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

    @Override
    public String getName() {
        return null;
    }
}
