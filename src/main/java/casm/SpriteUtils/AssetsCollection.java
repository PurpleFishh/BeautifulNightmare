package casm.SpriteUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class AssetsCollection {

    public static HashMap<String, Assets> sprites = new HashMap<>();
    public static Sprite blankSprite = new Sprite();

    public static void addSpritesheet(String sheetPath, int tileWidth, int tileHeight) {
        try {
            if (!sprites.containsKey(sheetPath)) {
                BufferedImage sprite_sheet = ImageLoader.LoadImage(sheetPath);
                sprites.put(sheetPath, new Assets(ImageLoader.LoadImage(sheetPath), tileWidth, tileHeight, sprite_sheet.getWidth(), sprite_sheet.getHeight()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Assets getSpritesheet(String sheetPath)
    {
        if(!sprites.containsKey(sheetPath))
            assert false : "There is no Spritesheet imported from: " + sheetPath;
        return sprites.get(sheetPath);
    }
}
