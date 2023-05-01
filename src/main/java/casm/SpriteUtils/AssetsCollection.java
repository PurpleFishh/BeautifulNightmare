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

    public HashMap<String, Assets> sprites = new HashMap<>();
    public Sprite blankSprite = new Sprite();

    public Assets addSpriteSheet(String sheetPath, int tileWidth, int tileHeight) {
        try {
            if (!sprites.containsKey(sheetPath)) {
                BufferedImage sprite_sheet = ImageLoader.getInstance().LoadImage(sheetPath);
                Assets asset =  new Assets(ImageLoader.getInstance().LoadImage(sheetPath), tileWidth, tileHeight, sprite_sheet.getWidth(), sprite_sheet.getHeight());
                sprites.put(sheetPath, asset);
                return asset;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Assets addSprite(String sheetPath) {
        try {
            if (!sprites.containsKey(sheetPath)) {
                BufferedImage sprite_sheet = ImageLoader.getInstance().LoadImage(sheetPath);
                Assets asset = new Assets(ImageLoader.getInstance().LoadImage(sheetPath), sprite_sheet.getWidth(), sprite_sheet.getHeight(), sprite_sheet.getWidth(), sprite_sheet.getHeight());
                sprites.put(sheetPath, asset);
                return asset;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Assets getSpriteSheet(String sheetPath)
    {
        assert sprites.containsKey(sheetPath) : "There is no Spritesheet imported from: " + sheetPath;
        return sprites.get(sheetPath);
    }
}
