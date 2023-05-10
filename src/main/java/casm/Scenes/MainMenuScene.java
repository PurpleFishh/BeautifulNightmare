package casm.Scenes;

import casm.ECS.Components.MouseListener;
import casm.ECS.Components.PositionComponent;
import casm.ECS.GameObject;
import casm.Factory.MenuFactory.MenuEntityFactory;
import casm.Factory.MenuFactory.MenuEntityType;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Objects.Menu.BackgroundImageObject;
import casm.Objects.Menu.Button;
import casm.Objects.Object;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Utils.Settings.Setting;
import casm.Utils.Vector2D;

public class MainMenuScene extends Scene {
    private Factory factory;

    public MainMenuScene() {
        factory = new MenuEntityFactory();
    }

    public enum layers {
        BACKGROUND,
        FOREGROUND
    }

    private void menuConstruct() {
        loadAssets();
        Sprite buttonsBg = AssetsCollection.getInstance().getSprite("menu_assets\\menu_buttons_bg.png");

        createObject(MenuEntityType.BACKGROUND_IMAGE, new Vector2D(), layers.BACKGROUND);
        BackgroundImageObject backButtons = (BackgroundImageObject) createObject(MenuEntityType.BACKGROUND_BUTTONS, new Vector2D(), layers.BACKGROUND);
        createObject(MenuEntityType.LOGO, new Vector2D(), layers.BACKGROUND);

        Vector2D buttonsPlace = backButtons.getSpawnPosition();

        Button button = (Button) createObject(MenuEntityType.BUTTON.PLAY, buttonsPlace, layers.FOREGROUND);
        button.getComponent(PositionComponent.class).position = ((Vector2D) buttonsPlace.clone()).center(button.getWidth(), button.getHeight(), buttonsBg.getWidth(), buttonsBg.getHeight());
        button = (Button) createObject(MenuEntityType.BUTTON.SETTINGS, buttonsPlace, layers.FOREGROUND);
        button.getComponent(PositionComponent.class).position = ((Vector2D) buttonsPlace.clone()).center(button.getWidth(), button.getHeight(), (int) (buttonsBg.getWidth() / 2), buttonsBg.getHeight());
        button = (Button) createObject(MenuEntityType.BUTTON.EXIT, buttonsPlace, layers.FOREGROUND);
        button.getComponent(PositionComponent.class).position = ((Vector2D) buttonsPlace.clone()).center(button.getWidth(), button.getHeight(), (int) (buttonsBg.getWidth() * 1.5), buttonsBg.getHeight());
    }

    private Object createObject(FactoryTypes type, Vector2D spawnPosition, layers layers) {
        Object entity = factory.create(type, spawnPosition);
        addGameObjectToScene(entity);
        addGameObjectToLayer(entity, layers.ordinal());
        return entity;
    }

    private void loadAssets() {
        AssetsCollection.getInstance().addSpriteSheet("menu_assets\\buttons\\buttons_spritesheet.png", 74, 58);
        AssetsCollection.getInstance().addSprite("menu_assets\\menu_background.png");
        AssetsCollection.getInstance().addSprite("menu_assets\\menu_buttons_bg.png");
        AssetsCollection.getInstance().addSprite("menu_assets\\logo.png");
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
            MouseListener.getInstance().unsubscribeAll();
            super.destroy();
        });
        th.start();
    }
}
