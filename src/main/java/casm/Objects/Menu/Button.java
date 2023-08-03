package casm.Objects.Menu;

import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.Rectangle;
import casm.ECS.Components.SpriteComponent;
import casm.Factory.MenuFactory.MenuEntityType;
import casm.Game;
import casm.Objects.Object;
import casm.Observer.ObserverMouse;
import casm.Scenes.Level.LevelSaverLoader;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Scenes.SceneType;
import casm.Utils.Vector2D;

import java.awt.event.MouseEvent;
import java.sql.SQLException;

/**
 * Object used for buttons
 * The buttons use {@link ObserverMouse} for mouse events.
 */
public class Button extends Object implements ObserverMouse {

    /**
     * The type of the button.
     */
    private MenuEntityType.BUTTON type;
    /**
     * If the button is hovered or not.
     */
    private boolean isHovered;
    /**
     * The asset of the button.
     */
    private Assets asset;
    /**
     * The index of the texture in the sprite sheet.
     */
    private int index;

    /**
     * @param name          - The name of the button.
     * @param spawnPosition - The position where the button will be displayed.
     * @param entityWidth   - The width of the button.
     * @param entityHeight  - The height of the button.
     */
    public Button(String name, Vector2D spawnPosition, int entityWidth, int entityHeight) {
        super(name, spawnPosition, entityWidth, entityHeight);
    }

    /**
     * @param name          - The name of the button.
     * @param spawnPosition - The position where the button will be displayed.
     */
    public Button(String name, Vector2D spawnPosition) {
        super(name, spawnPosition);
    }

    /**
     * Update the dimensions of the button.
     *
     * @param width  new width of the button
     * @param height new height of the button
     */
    public void updateDimensions(int width, int height) {
        super.updateDimensions(width, height);
    }

    /**
     * This method is called by the {@link casm.Observer.Observable} class when a mouse event is triggered.<br>
     * e.g. When the mouse is clicked, when the mouse moves.
     *
     * @param event The mouse event.
     */
    @Override
    public void notify(MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_CLICKED) {
            Rectangle collider = this.getComponent(ColliderComponent.class).getCollider(ColliderType.BUTTON);
            if (collider.contains(event.getX(), event.getY())) {
                if (type == MenuEntityType.BUTTON.PLAY) {
                    if (Game.getLevelScene() == null) {
                        //Game.changeScene(SceneType.LEVEL);
                        try {
                            Game.changeLevel(LevelSaverLoader.getInstance().getSavedLevelIndex(), true);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        Game.destroyAllWithoutTopScenes();
                    } else {
                        Game.destroyViewingScene();
                    }
                } else if (type == MenuEntityType.BUTTON.RETRY) {
                    Game.changeLevel(Game.getLevelScene().getLevel(), false);
                } else if (type == MenuEntityType.BUTTON.EXIT) {
                    if (Game.getCurrentScene().getType() == SceneType.MAIN_MENU)
                        Game.exitGame();
                    else {
                        //Game.destroyViewingScene();
                        Game.changeScene(SceneType.MAIN_MENU);
                        Game.destroySecondScenes();
                        //Game.destroyAllWithoutTopScenes();
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

    /**
     * Change the texture of the button if it is hovered or not.
     *
     * @param isHovered - If the button is hovered.
     */
    public void hover(boolean isHovered) {
        Sprite buttonTexture = asset.getSprite(index + (isHovered ? 1 : 0));
        this.getComponent(SpriteComponent.class).setSprite(buttonTexture);
    }

    /**
     * @param type - The type of the button.
     */
    public void setType(MenuEntityType.BUTTON type) {
        this.type = type;
    }

    /**
     * @return - The type of the button.
     */
    public MenuEntityType.BUTTON getType() {
        return type;
    }

    /**
     * @param asset - The asset of the button.
     * @param index - The index of the texture in the sprite sheet.
     */
    public void setAsset(Assets asset, int index) {
        this.asset = asset;
        this.index = index;
    }
}
