package casm.Factory.MenuFactory;

import casm.Builder.BuilderDirector;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Objects.Menu.*;
import casm.Objects.Object;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Utils.Settings.Setting;
import casm.Utils.Vector2D;

/**
 * Factory for creating menu objects
 */
public class MenuEntityFactory implements Factory {
    /**
     * The director used for building buttons
     */
    private final BuilderDirector builder;

    public MenuEntityFactory() {
        this.builder = new ButtonsBuilderDirector();
    }

    /**
     * Used to creat a menu object
     *
     * @param type          the type of the object
     * @param spawnPosition the position where it will be spawned
     * @return the created object
     */
    @Override
    public Object create(FactoryTypes type, Vector2D spawnPosition) {
        if (type instanceof MenuEntityType.BUTTON) {
            return builder.makeObject((MenuEntityType.BUTTON) type, spawnPosition);
        } else if (type == MenuEntityType.BACKGROUND_IMAGE) {
            Sprite bgImage = AssetsCollection.getInstance().addSprite("menu_assets\\menu_background.png");
            return new BackgroundImageObject(new Vector2D(0, 0), bgImage,
                    Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT);
        } else if (type == MenuEntityType.LOGO) {
            Sprite logo = AssetsCollection.getInstance().addSprite("menu_assets\\logo.png");
            return new BackgroundImageObject(new Vector2D(0, 0).center(
                    logo.getWidth() * 1.5, logo.getHeight() * 1.5,
                    Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT / 1.8), logo,
                    (int)(logo.getWidth() * 1.5), (int)(logo.getHeight() * 1.5));
        } else if (type == MenuEntityType.BACKGROUND_BUTTONS) {
            Sprite buttonsBg = AssetsCollection.getInstance().addSprite("menu_assets\\menu_buttons_bg.png");
            Vector2D buttonsPlace = new Vector2D().center(buttonsBg, Setting.SCREEN_WIDTH,
                    Setting.SCREEN_HEIGHT * 1.5);
            return new BackgroundImageObject(buttonsPlace, buttonsBg);
        } else if (type == MenuEntityType.BACKGROUND_SETTINGS) {
            Sprite buttonsBg = AssetsCollection.getInstance().addSprite("menu_assets\\settings_buttons_bg.png");
            Vector2D buttonsPlace = new Vector2D().center(buttonsBg, Setting.SCREEN_WIDTH,
                    Setting.SCREEN_HEIGHT * 1.1);
            return new BackgroundImageObject(buttonsPlace, buttonsBg);
        } else if (type == MenuEntityType.TITLE_BAR) {
            Sprite logo = AssetsCollection.getInstance().addSprite("menu_assets\\title_bg.png");
            return new BackgroundImageObject(new Vector2D(0, 0).center(logo,
                    Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT / 2.5), logo);
        } else {
            return null;
        }
    }
}