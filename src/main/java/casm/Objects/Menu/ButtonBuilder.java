package casm.Objects.Menu;

import casm.Builder.Builder;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.MouseListener;
import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.Factory.MenuFactory.MenuEntityType;
import casm.Objects.Object;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Utils.Vector2D;

public class ButtonBuilder implements Builder {
    private Button objectBuilt = new Button("button", new Vector2D());

    public ButtonBuilder setType(MenuEntityType.BUTTON type)
    {
        objectBuilt.setType(type);
        return this;
    }

    public ButtonBuilder addImage() {
        Sprite buttonTexture = AssetsCollection.getInstance().getSpriteSheet("menu_assets\\buttons\\buttons_spritesheet.png")
                .getSprite(objectBuilt.getType().ordinal() * 2);
        objectBuilt.updateDimensions(buttonTexture.getWidth(), buttonTexture.getHeight());
        objectBuilt.addComponent(new SpriteComponent(buttonTexture));
        objectBuilt.addComponent(new ColliderComponent(ColliderType.BUTTON, buttonTexture.getWidth(), buttonTexture.getHeight()));

        return this;
    }
    public ButtonBuilder clickable()
    {
        MouseListener.getInstance().subscribe(objectBuilt);
        return this;
    }

    public ButtonBuilder setPosition(Vector2D position) {
        objectBuilt.getComponent(PositionComponent.class).position = position;
        return this;
    }

    @Override
    public Object build() {
        return objectBuilt;
    }

    @Override
    public void reset() {
        objectBuilt = new Button("button", new Vector2D());
    }
}
