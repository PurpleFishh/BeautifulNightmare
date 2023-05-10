package casm.SpriteUtils;

import casm.Utils.Vector2D;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Assets {

    private final List<Sprite> assets = new ArrayList<>();
    private int imageWidth, imageHeight;

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
    public Assets(BufferedImage image, int xOffset, int yOffset, int tileWidth, int tileHeight, int frameWidth, int frameHeight, int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(image, tileWidth, tileHeight);

        /// Se obtin subimaginile corespunzatoare elementelor necesare.
        for(int x = 0; x < imageWidth / frameWidth; ++x)
                assets.add(new Sprite(sheet.cropAtPosition(xOffset + frameWidth * x, yOffset), tileWidth, tileHeight, new Vector2D(x, 0)));
    }

    public List<Sprite> getAssets() {
        return assets;
    }

    public Sprite getSprite(int index) {
        return assets.get(index);
    }
    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }
}
