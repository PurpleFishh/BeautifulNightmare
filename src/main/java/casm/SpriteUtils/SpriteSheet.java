package casm.SpriteUtils;

import casm.Utils.Vector2D;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage       spriteSheet;              /*!< Referinta catre obiectul BufferedImage ce contine sprite sheet-ul.*/
    private int tileWidth;   /*!< Latimea unei dale din sprite sheet.*/
    private int tileHeight;   /*!< Inaltime unei dale din sprite sheet.*/

    public SpriteSheet(BufferedImage buffImg, int tileWidth, int tileHeight)
    {
        /// Retine referinta catre BufferedImage object.
        spriteSheet = buffImg;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public BufferedImage crop(int x, int y)
    {
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }
    public BufferedImage cropAtPosition(int x, int y)
    {
        return spriteSheet.getSubimage(x, y, tileWidth, tileHeight);
    }
    public BufferedImage crop(Vector2D coord)
    {
        return spriteSheet.getSubimage((int) (coord.x * tileWidth), (int) (coord.y * tileHeight), tileWidth, tileHeight);
    }


}
