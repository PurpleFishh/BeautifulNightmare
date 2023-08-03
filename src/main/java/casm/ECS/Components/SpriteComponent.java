package casm.ECS.Components;

import casm.ECS.Component;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Utils.FlipEntityMediator;
import casm.Utils.Renderer;
import casm.Utils.Vector2D;

import java.awt.image.BufferedImage;

/**
 * SpriteComponent will draw a sprite at the entity position<br>
 * Dependencies: the entity needs to have {@link PositionComponent}
 */
public class SpriteComponent extends Component {

    /**
     * The sprite that will be rendered for the entity
     */
    private Sprite sprite;

    /**
     * The position of the entity, {@link PositionComponent}
     */
    private PositionComponent positionComp;
    /**
     * If the sprite is flipped vertically
     */
    private boolean flipped_vertically = false;
    /**
     * If the sprite is flipped horizontally
     */
    private boolean antidiagonal_flipped = false;
    private boolean flipped_horizontally = false;
    /**
     * width - image render width <br>
     * height - image render height
     */
    private int width = 0, height = 0;
    /**
     * If the sprite needs an anti-diagonal flip, it will be done at the initialization on a copy of the sprite
     */
    private BufferedImage antiDiagonalFlip = null;

    /**
     * Create a {@link SpriteComponent} with a blank sprite
     */
    public SpriteComponent() {
        sprite = AssetsCollection.getInstance().blankSprite;
    }

    /**
     * Create a {@link SpriteComponent}
     *
     * @param sprite the sprite that will be rendered
     */
    public SpriteComponent(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * Create a {@link SpriteComponent}
     *
     * @param sprite the sprite that will be rendered
     * @param width  render image width
     * @param height render image height
     */
    public SpriteComponent(Sprite sprite, int width, int height) {
        this.sprite = sprite;
        this.width = width;
        this.height = height;
    }


    /**
     * Create a {@link SpriteComponent}
     *
     * @param sprite               the sprite that will be rendered
     * @param flipped_vertically   if it is flipped vertically
     * @param flipped_horizontally if it is flipped horizontally
     */
    public SpriteComponent(Sprite sprite, boolean flipped_vertically, boolean flipped_horizontally) {
        this.sprite = sprite;
        this.flipped_vertically = flipped_vertically;
        this.flipped_horizontally = flipped_horizontally;
    }

    /**
     * Create a {@link SpriteComponent}
     *
     * @param sprite                the sprite that will be rendered
     * @param flipped_vertically    if it is flipped vertically
     * @param flipped_horizontally  if it is flipped horizontally
     * @param anti_diagonal_flipped if it is flipped anti-diagonally
     */
    public SpriteComponent(Sprite sprite, boolean flipped_vertically, boolean flipped_horizontally, boolean anti_diagonal_flipped) {
        this.sprite = sprite;
        this.flipped_vertically = flipped_vertically;
        this.flipped_horizontally = flipped_horizontally;
        this.antidiagonal_flipped = anti_diagonal_flipped;
    }

    /**
     * Initialize the component
     */
    @Override
    public void init() {
        positionComp = gameObject.getComponent(PositionComponent.class);
        if (antidiagonal_flipped) {
            antiDiagonalFlip = FlipEntityMediator.getInstance().antiDiagonalFlip(sprite.getTexture());
        }
    }

    /**
     * Draw the sprite at the entity position
     */
    @Override
    public void draw() {
        if (gameObject.isAlive()) {
            if(sprite.getTexture() != null) {
                /// To center the sprite in the entity "hit-box" so if the sprite is bigger, it will be centered
                if (positionComp != null)
                    if ((positionComp.getHeight() > 0 && positionComp.getHeight() < sprite.getHeight()) ||
                            (positionComp.getWidth() > 0 && positionComp.getWidth() < sprite.getWidth())) {
                        Vector2D offset = new Vector2D((double) (sprite.getWidth() - positionComp.getWidth()) / 2,
                                sprite.getHeight() - positionComp.getHeight());
                        offset = ((Vector2D) positionComp.position.clone()).sub(offset);
                        Renderer.getInstance().drawImage(antiDiagonalFlip == null ? sprite.getTexture() : antiDiagonalFlip, offset, width == 0 ? sprite.getWidth() : width,
                                height == 0 ? sprite.getHeight() : height, flipped_vertically, flipped_horizontally);
                        return;
                    }
                Renderer.getInstance().drawImage(antiDiagonalFlip == null ? sprite.getTexture() : antiDiagonalFlip, positionComp.position, width == 0 ? sprite.getWidth() : width,
                        height == 0 ? sprite.getHeight() : height, flipped_vertically, flipped_horizontally);
            }
        }
    }

    /**
     * Flip the sprite vertically
     */
    public void flipVertically() {
        flipped_vertically = !flipped_vertically;
    }

    /**
     * Set the sprite flip vertically flag
     *
     * @param flip if it is flipped vertically or not
     */
    public void setFlippedVertically(boolean flip) {
        flipped_vertically = flip;
    }

    /**
     * Flip the sprite horizontally
     */
    public void flipHorizontally() {
        flipped_horizontally = !flipped_horizontally;
    }

    /**
     * Set the sprite flip horizontally flag
     *
     * @param flip if it is flipped horizontally or not
     */
    public void setFlippedHorizontally(boolean flip) {
        flipped_horizontally = flip;
    }

    /**
     * Set a new sprite for the component to render
     *
     * @param sprite the new sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
