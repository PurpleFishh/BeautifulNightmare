package casm.Factory.MenuFactory;

import casm.Builder.BuilderDirector;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Objects.Menu.*;
import casm.Objects.Object;
import casm.Scenes.MainMenuScene;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Utils.Settings.Setting;
import casm.Utils.Vector2D;

public class MenuEntityFactory implements Factory {
    private BuilderDirector builder;

    public MenuEntityFactory() {
        this.builder = new ButtonsBuilderDirector();
    }

    @Override
    public Object create(FactoryTypes type, Vector2D spawnPosition) {
        if (type instanceof MenuEntityType.BUTTON) {
            return builder.makeObject((MenuEntityType.BUTTON) type, spawnPosition);
        } else if (type == MenuEntityType.BACKGROUND_IMAGE) {
            Sprite bgImage = AssetsCollection.getInstance().getSprite("menu_assets\\menu_background.png");
            return new BackgroundImageObject(new Vector2D(0, 0),bgImage,
                    Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT);
        } else if (type == MenuEntityType.LOGO) {
            Sprite logo = AssetsCollection.getInstance().getSprite("menu_assets\\logo.png");
            return new BackgroundImageObject(new Vector2D(0, 0).center(logo,
                    Setting.SCREEN_WIDTH, (int) (Setting.SCREEN_HEIGHT / 1.8)), logo);
        } else if (type == MenuEntityType.BACKGROUND_BUTTONS) {
            Sprite buttonsBg = AssetsCollection.getInstance().getSprite("menu_assets\\menu_buttons_bg.png");
            Vector2D buttonsPlace = new Vector2D().center(buttonsBg, Setting.SCREEN_WIDTH,
                    (int) (Setting.SCREEN_HEIGHT * 1.5));
            return new BackgroundImageObject(buttonsPlace, buttonsBg);
        } else {
            return null;
        }
    }
}