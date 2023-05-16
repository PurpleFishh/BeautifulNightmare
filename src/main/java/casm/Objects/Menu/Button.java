package casm.Objects.Menu;

import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.Rectangle;
import casm.ECS.Components.SpriteComponent;
import casm.Factory.MenuFactory.MenuEntityType;
import casm.Game;
import casm.Objects.Object;
import casm.Observer.ObserverMouse;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Scenes.SceneType;
import casm.Utils.Vector2D;

import java.awt.event.MouseEvent;

public class Button extends Object implements ObserverMouse {

    private MenuEntityType.BUTTON type;
    private boolean isHovered;
    private Assets asset;
    private int index;

    public Button(String name, Vector2D spawnPosition, int entityWidth, int entityHeight) {
        super(name, spawnPosition, entityWidth, entityHeight);
    }

    public Button(String name, Vector2D spawnPosition) {
        super(name, spawnPosition);
    }

    public void updateDimensions(int width, int height) {
        super.updateDimensions(width, height);
    }

    @Override
    public void notify(MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_CLICKED) {
            Rectangle collider = this.getComponent(ColliderComponent.class).getCollider(ColliderType.BUTTON);
            if (collider.contains(event.getX(), event.getY())) {
                if (type == MenuEntityType.BUTTON.PLAY || type == MenuEntityType.BUTTON.RETRY) {
                    Game.changeScene(SceneType.LEVEL);
                    Game.destroyAllWithoutTopScenes();
                } else if (type == MenuEntityType.BUTTON.EXIT) {
                    if (Game.getCurrentScene().getType() == SceneType.MAIN_MENU)
                        Game.exitGame();
                    else {
                        Game.changeScene(SceneType.MAIN_MENU);
                        Game.destroyAllWithoutTopScenes();
                    }
                } else if (type == MenuEntityType.BUTTON.SETTINGS)
                    Game.changeScene(SceneType.SETTINGS_MENU);
                else if (type == MenuEntityType.BUTTON.BACK || type == MenuEntityType.BUTTON.RESUME)
                    Game.destroyViewingScene();
                System.out.println("Da am fost apasat bro" + getType());
            }
        } else if (event.getID() == MouseEvent.MOUSE_MOVED) {
            Rectangle collider = this.getComponent(ColliderComponent.class).getCollider(ColliderType.BUTTON);
            if (collider.contains(event.getX(), event.getY())) {
                if (!isHovered) {
                    isHovered = true;
                    hover(true);
                }
            } else if (isHovered) {
                isHovered = false;
                hover(false);
            }

        }
    }

    public void hover(boolean isHovered) {
        Sprite buttonTexture = asset.getSprite(index + (isHovered ? 1 : 0));
        this.getComponent(SpriteComponent.class).setSprite(buttonTexture);
    }

    public void setType(MenuEntityType.BUTTON type) {
        this.type = type;
    }

    public MenuEntityType.BUTTON getType() {
        return type;
    }

    public void setAsset(Assets asset, int index) {
        this.asset = asset;
        this.index = index;
    }
}
