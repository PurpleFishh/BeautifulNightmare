package casm.Objects.Menu;

import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.Rectangle;
import casm.ECS.GameObject;
import casm.Factory.MenuFactory.MenuEntityType;
import casm.Game;
import casm.Objects.Object;
import casm.Observer.Observable;
import casm.Observer.Observer;
import casm.Observer.ObserverMouse;
import casm.Utils.Vector2D;

import java.awt.event.MouseEvent;

public class Button extends Object implements ObserverMouse {

    private MenuEntityType.BUTTON type;

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
                if (type == MenuEntityType.BUTTON.PLAY)
                    Game.changeScene(0);
                System.out.println("Da am fost apasat bro" + getType());
            }
        }
    }

    public void setType(MenuEntityType.BUTTON type) {
        this.type = type;
    }

    public MenuEntityType.BUTTON getType() {
        return type;
    }
}
