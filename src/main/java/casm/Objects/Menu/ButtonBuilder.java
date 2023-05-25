package casm.Objects.Menu;

import casm.Builder.Builder;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.MouseListener;
import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.Factory.MenuFactory.MenuEntityType;
import casm.Game;
import casm.Objects.Object;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Utils.Vector2D;

/**
 * Used to build buttons
 */
public class ButtonBuilder implements Builder {
    /**
     * The button that will be built
     */
    private Button objectBuilt = new Button("button", new Vector2D());

    /**
     * @param type - The type of the button.
     * @return - Reference to the builder.
     */
    public ButtonBuilder setType(MenuEntityType.BUTTON type)
    {
        objectBuilt.setType(type);
        return this;
    }

    /**
     * Sets the image to the button and adds its collider.
     * @param sheetPath - The path to the sprite sheet.
     * @return - Reference to the builder.
     */
    public ButtonBuilder addImage(String sheetPath) {
        //TODO: fa diferenta dintre alea mari si alea mici
        Sprite buttonTexture;
        int index = objectBuilt.getType().ordinal();
        if(objectBuilt.getType() !=  MenuEntityType.BUTTON.BACK) {
            index *= 2;
            buttonTexture = AssetsCollection.getInstance().getSpriteSheet(sheetPath)
                    .getSprite(index);
        }
        else {
            index -= (MenuEntityType.BUTTON.BACK.ordinal());
            index *= 2;
            buttonTexture = AssetsCollection.getInstance().getSpriteSheet(sheetPath)
                    .getSprite((index));
        }
        objectBuilt.updateDimensions(buttonTexture.getWidth(), buttonTexture.getHeight());
        objectBuilt.addComponent(new SpriteComponent(buttonTexture));
        objectBuilt.addComponent(new ColliderComponent(ColliderType.BUTTON, buttonTexture.getWidth(), buttonTexture.getHeight()));
        objectBuilt.setAsset(AssetsCollection.getInstance().getSpriteSheet(sheetPath), index);

        return this;
    }

    /**
     * Make the button clickable(It will be notified by the {@link casm.Observer.Observable}).
     * @return - Reference to the builder.
     */
    public ButtonBuilder clickable()
    {
        MouseListener.getInstance().subscribe(Game.getCurrentScene(),objectBuilt);
        return this;
    }

    /**
     * @param position - The position where the button will be displayed.
     * @return - Reference to the builder.
     */
    public ButtonBuilder setPosition(Vector2D position) {
        objectBuilt.getComponent(PositionComponent.class).position = position;
        return this;
    }

    /**
     * @return - The button that was built.
     */
    @Override
    public Object build() {
        return objectBuilt;
    }

    /**
     * Resets the button that will be built.
     */
    @Override
    public void reset() {
        objectBuilt = new Button("button", new Vector2D());
    }
}
