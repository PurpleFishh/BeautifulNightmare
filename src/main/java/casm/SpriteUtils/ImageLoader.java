package casm.SpriteUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ImageLoader {
    private static ImageLoader instance = null;
    private ImageLoader(){}
    public static ImageLoader getInstance()
    {
        if(instance == null)
            instance = new ImageLoader();
        return instance;
    }

    public BufferedImage LoadImage(String path) throws IOException {
        /// Avand in vedere exista situatii in care fisierul sursa sa nu poate fi accesat
        /// metoda read() arunca o excpetie ce trebuie tratata
        /// Clasa ImageIO contine o serie de metode statice pentru file IO.
        /// Metoda read() are ca argument un InputStream construit avand ca referinta
        /// directorul res, director declarat ca director de resurse in care se gasesc resursele
        /// proiectului sub forma de fisiere sursa.
        File file = new File(Paths.get("").toAbsolutePath() + "\\resources\\" + path);
        return ImageIO.read(file);
    }

}
