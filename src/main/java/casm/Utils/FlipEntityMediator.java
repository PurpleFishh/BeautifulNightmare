package casm.Utils;

import casm.ECS.Components.AttackComponent;
import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A utility class used to flip entities
 */
public class FlipEntityMediator {

    /**
     * Singleton instance
     */
    private static FlipEntityMediator instance = null;

    private FlipEntityMediator() {
    }

    /**
     * @return get the singleton instance
     */
    public static FlipEntityMediator getInstance() {
        if (instance == null)
            instance = new FlipEntityMediator();
        return instance;
    }

    /**
     * Flip a game object vertically
     *
     * @param gameObject the game object to be flipped
     * @param flip       if the game object should be flipped
     */
    public void flipVertically(GameObject gameObject, boolean flip) {
        if (gameObject.getComponent(PositionComponent.class).isCanMove()) {
            if (gameObject.hasComponent(SpriteComponent.class))
                gameObject.getComponent(SpriteComponent.class).setFlippedVertically(flip);
            if (gameObject.hasComponent(AttackComponent.class))
                gameObject.getComponent(AttackComponent.class).setFlipColliderVertically(flip);
        }
    }

    /**
     * Flip a game object horizontally
     *
     * @param gameObject the game object to be flipped
     * @param flip       if the game object should be flipped
     */
    public void flipHorizontally(GameObject gameObject, boolean flip) {
        if (gameObject.getComponent(PositionComponent.class).isCanMove()) {
            if (gameObject.hasComponent(SpriteComponent.class))
                gameObject.getComponent(SpriteComponent.class).setFlippedHorizontally(flip);
            if (gameObject.hasComponent(AttackComponent.class))
                gameObject.getComponent(AttackComponent.class).setFlipColliderHorizontally(flip);
        }
    }

    /**
     * Flip a image anti-diagonally
     *
     * @param image the image to be flipped
     * @return the flipped image
     */
    public BufferedImage antiDiagonalFlip(BufferedImage image) {
        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D graphics2D = newImg.createGraphics();
        // 90 + horoznotally
        graphics2D.rotate(Math.toRadians(90), image.getWidth() / 2, image.getHeight() / 2);
        graphics2D.drawImage(image, 0, image.getHeight(), image.getWidth(), -image.getHeight(), null);

        return newImg;
    }

}
