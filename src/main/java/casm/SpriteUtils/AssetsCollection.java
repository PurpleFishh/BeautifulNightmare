package casm.SpriteUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * AssetsCollection is used to store all the assets of the game.
 * Implementing the Flyweight and Singleton design pattern.
 */
public class AssetsCollection {
    /**
     * Singleton instance.
     */
    private static AssetsCollection instance = null;
    private AssetsCollection(){}

    /**
     * @return get the singleton instance.
     */
    public static AssetsCollection getInstance()
    {
        if(instance == null)
            instance = new AssetsCollection();
        return instance;
    }

    /**
     * Map that contains all the sprite sheets stored by path and assets.
     */
    public HashMap<String, Assets> sprite_sheets = new HashMap<>();
    /**
     * Map that contains all the sprites stored by path and sprite.
     */
    public HashMap<String, Sprite> sprites = new HashMap<>();
    /**
     * Blank sprite, without any image.
     */
    public Sprite blankSprite = new Sprite();

    /**
     * This method loads a sprite sheet from a file to an Assets object if it was not already loaded if so it just returns it.
     * @param sheetPath path to the sprite sheet.
     * @param tileWidth width of a tile in the sprite sheet.
     * @param tileHeight height of a tile in the sprite sheet.
     * @return the assets of the sprite sheet.
     */
    public Assets addSpriteSheet(String sheetPath, int tileWidth, int tileHeight) {
        try {
            if (!sprite_sheets.containsKey(sheetPath)) {
                Assets asset =  new Assets(ImageLoader.getInstance().LoadImage(sheetPath), tileWidth, tileHeight);
                sprite_sheets.put(sheetPath, asset);
                return asset;
            }else
                return sprite_sheets.get(sheetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method loads a sprite from a file to a Sprite object if it was not already loaded if so it just returns it.
     * @param sheetPath path to the sprite sheet.
     * @return the sprite sheet.
     */
    public Sprite addSprite(String sheetPath) {
        try {
            if (!sprites.containsKey(sheetPath)) {
                Sprite sprite = new Sprite(ImageLoader.getInstance().LoadImage(sheetPath));
                sprites.put(sheetPath, sprite);
                return sprite;
            }else
                return sprites.get(sheetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get an already loaded sprite sheet.
     * @param sheetPath path to the sprite sheet.
     * @return the assets of the sprite sheet.
     */
    public Assets getSpriteSheet(String sheetPath)
    {
        assert sprite_sheets.containsKey(sheetPath) : "There is no Spritesheet imported from: " + sheetPath;
        return sprite_sheets.get(sheetPath);
    }

    /**
     * Get an already loaded sprite.
     * @param sheetPath path to the sprite sheet.
     * @return the sprite.
     */
    public Sprite getSprite(String sheetPath)
    {
        assert sprites.containsKey(sheetPath) : "There is no Spritesheet imported from: " + sheetPath;
        return sprites.get(sheetPath);
    }
}
