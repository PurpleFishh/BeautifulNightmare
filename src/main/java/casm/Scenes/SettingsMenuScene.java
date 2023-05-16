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
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.StateMachine.AnimationStateMachine.State;
import casm.Utils.Renderer;
import casm.Utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class SettingsMenuScene extends Scene implements State {
    private Factory factory;
    private String name;
    private ArrayList<Button> buttons = new ArrayList<>();

    public SettingsMenuScene(SceneType type) {
        super(type);
        factory = new MenuEntityFactory();
        name = "Settings Menu";
    }

    public enum layers {
        BACKGROUND,
        FOREGROUND
    }

    private void menuConstruct() {
        Sprite buttonsBg = AssetsCollection.getInstance().getSprite("menu_assets\\settings_buttons_bg.png");

        createObject(MenuEntityType.BACKGROUND_IMAGE, new Vector2D(), layers.BACKGROUND);
        BackgroundImageObject titleBar = (BackgroundImageObject) createObject(MenuEntityType.TITLE_BAR, new Vector2D(), layers.BACKGROUND);
        BackgroundImageObject buttonsBG = (BackgroundImageObject) createObject(MenuEntityType.BACKGROUND_SETTINGS,
                new Vector2D(), layers.BACKGROUND);

        Vector2D buttonsPlace = buttonsBG.getSpawnPosition().add(new Vector2D(buttonsBG.getWidth(), buttonsBG.getHeight()));
        buttons.add((Button) createObject(MenuEntityType.BUTTON.BACK, buttonsPlace.sub(new Vector2D(90, 75)), layers.FOREGROUND));

        //TODO: fa cumva mai frumoasa crearea de text
        GameObject text = new GameObject("Text");
        text.addComponent(new TextComponent("Settings", new Vector2D(0, 0), "Eras Bold ITC", 30,
                new Color(219, 246, 255)));
        Vector2D size = text.getComponent(TextComponent.class).getSize();
        text.getComponent(TextComponent.class).setPosition(new Vector2D(titleBar.getSpawnPosition()).center(size.x, size.y,
                titleBar.getWidth(), titleBar.getHeight() - size.y / 2));
        addGameObjectToScene(text);
        addGameObjectToLayer(text, layers.FOREGROUND.ordinal());
    }

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
