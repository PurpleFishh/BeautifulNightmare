package casm.SpriteUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class AssetsCollection {
    private static AssetsCollection instance = null;
    private AssetsCollection(){}
    public static AssetsCollection getInstance()
    {
        if(instance == null)
            instance = new AssetsCollection();
        return instance;
    }

    public HashMap<String, Assets> sprite_sheets = new HashMap<>();
    public HashMap<String, Sprite> sprites = new HashMap<>();
    public Sprite blankSprite = new Sprite();

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

    public Assets getSpriteSheet(String sheetPath)
    {
        assert sprite_sheets.containsKey(sheetPath) : "There is no Spritesheet imported from: " + sheetPath;
        return sprite_sheets.get(sheetPath);
    }
    public Sprite getSprite(String sheetPath)
    {
        assert sprites.containsKey(sheetPath) : "There is no Spritesheet imported from: " + sheetPath;
        return sprites.get(sheetPath);
    }
}
