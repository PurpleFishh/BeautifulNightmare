package casm.Utils;

import casm.ECS.Components.AttackComponent;
import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;

public class FlipEntityMediator {

    private static FlipEntityMediator instance = null;
    private FlipEntityMediator() {
    }

    public static FlipEntityMediator getInstance() {
        if (instance == null)
            instance = new FlipEntityMediator();
        return instance;
    }

    public void flipVertically(GameObject gameObject, boolean flip)
    {
        if(gameObject.hasComponent(SpriteComponent.class))
            gameObject.getComponent(SpriteComponent.class).setFlippedVertically(flip);
        if(gameObject.hasComponent(AttackComponent.class))
            gameObject.getComponent(AttackComponent.class).setFlipColliderVertically(flip);
    }

    public void flipHorizontally(GameObject gameObject, boolean flip)
    {
        if(gameObject.hasComponent(SpriteComponent.class))
            gameObject.getComponent(SpriteComponent.class).setFlippedHorizontally(flip);
        if(gameObject.hasComponent(AttackComponent.class))
            gameObject.getComponent(AttackComponent.class).setFlipColliderHorizontally(flip);
    }

}
