package casm.ECS.Components;

import casm.ECS.Component;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Utils.Renderer;
import casm.Utils.Vector2D;

public class SpriteComponent extends Component {

    private Sprite sprite;
    private PositionComponent positionComp;
    private boolean flipped_vertically = false;
    private boolean flipped_horizontally = false;

    public SpriteComponent() {
        sprite = AssetsCollection.blankSprite;
    }
    public SpriteComponent(Sprite sprite) {
        this.sprite = sprite;
    }


    public SpriteComponent(Sprite sprite, boolean flipped_vertically, boolean flipped_horizontally) {
        this.sprite = sprite;
        this.flipped_vertically = flipped_vertically;
        this.flipped_horizontally = flipped_horizontally;
    }

    @Override
    public void init() {
        positionComp = gameObject.getComponent(PositionComponent.class);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {
        /// To center the sprite in the entity "hit-box" so if the sprite is bigger, it will be centered
        if(positionComp.getHeight() < sprite.getHeight() || positionComp.getWidth() < sprite.getWidth())
        {
            Vector2D offset = new Vector2D((double) (sprite.getWidth() - positionComp.getWidth()) / 2,
                    sprite.getHeight() - positionComp.getHeight());
            offset = ((Vector2D) positionComp.position.clone()).sub(offset);
            Renderer.drawImage(sprite.getTexture(), offset, sprite.getWidth(), sprite.getHeight(),
                    flipped_vertically, flipped_horizontally);
        }else
            Renderer.drawImage(sprite.getTexture(), positionComp.position, sprite.getWidth(), sprite.getHeight(),
                    flipped_vertically, flipped_horizontally);
    }

    public void flipVertically() {
        flipped_vertically = !flipped_vertically;
    }

    public void setFlippedVertically(boolean flip) {
        flipped_vertically = flip;
    }

    public void flipHorizontally() {
        flipped_horizontally = !flipped_horizontally;
    }

    public void setFlippedHorizontally(boolean flip) {
        flipped_horizontally = flip;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
