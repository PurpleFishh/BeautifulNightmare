package casm.SpriteUtils;

import casm.Utils.Vector2D;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Assets are used to store a list of sprites and their dimensions.
 */
public class Assets {

    /**
     * The list of sprites contained by the asset.
     */
    private final List<Sprite> assets = new ArrayList<>();
    /**
     * <b>imageWidth</b> and <b>imageHeight</b> are the dimensions of the image.
     */
    private final int imageWidth;
    private final int imageHeight;

    /**
     * Give the sprite sheet to the method and the dimensions of a tile, and it will cut and store the tiles.
     * @param image BufferedImage object that contains the sprite sheet.
     * @param tileWidth width of a tile in the sprite sheet.
     * @param tileHeight height of a tile in the sprite sheet.
     */
    public Assets(BufferedImage image, int tileWidth, int tileHeight) {
        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();
        /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(image, tileWidth, tileHeight);

        /// Se obtin subimaginile corespunzatoare elementelor necesare.
        for(int y = 0; y < imageHeight / tileHeight; ++y)
            for(int x = 0; x < imageWidth / tileWidth; ++x)
                assets.add(new Sprite(sheet.crop(x, y), tileWidth, tileHeight, new Vector2D(x, y)));
    }

    /**
     * Give the sprite sheet to the method and the dimensions of a tile, and it will cut and store the tiles.
     * @param image BufferedImage object that contains the sprite sheet.
     * @param tileWidth width of a tile in the sprite sheet.
     * @param tileHeight height of a tile in the sprite sheet.
     * @param imageWidth width of the image.
     * @param imageHeight height of the image.
     */
    public Assets(BufferedImage image, int tileWidth, int tileHeight, int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(image, tileWidth, tileHeight);

        /// Se obtin subimaginile corespunzatoare elementelor necesare.
        for(int y = 0; y < imageHeight / tileHeight; ++y)
            for(int x = 0; x < imageWidth / tileWidth; ++x)
                assets.add(new Sprite(sheet.crop(x, y), tileWidth, tileHeight, new Vector2D(x, y)));
    }

    /**
     * Used to load animation frame from the animation sheet.
     * @param image BufferedImage object that contains the sprite sheet.
     * @param xOffset the x coordinate of the first frame.
     * @param yOffset the y coordinate of the first frame.
     * @param tileWidth width of a tile in the sprite sheet.
     * @param tileHeight height of a tile in the sprite sheet.
     * @param frameWidth width of a frame.
     * @param frameHeight height of a frame.
     * @param imageWidth width of the image.
     * @param imageHeight height of the image.
     */
    public Assets(BufferedImage image, int xOffset, int yOffset, int tileWidth, int tileHeight, int frameWidth, int frameHeight, int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(image, tileWidth, tileHeight);

        /// Se obtin subimaginile corespunzatoare elementelor necesare.
        for(int x = 0; x < imageWidth / frameWidth; ++x)
                assets.add(new Sprite(sheet.cropAtPosition(xOffset + frameWidth * x, yOffset), tileWidth, tileHeight, new Vector2D(x, 0)));
    }

    /**
     * @return the list of sprites.
     */
    public List<Sprite> getAssets() {
        return assets;
    }

    /**
     * @param index the index of the sprite.
     * @return the sprite at the given index.
     */
    public Sprite getSprite(int index) {
        return assets.get(index);
    }

    /**
     * @return the width of the image.
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @return the height of the image.
     */
    public int getImageHeight() {
        return imageHeight;
    }
}
