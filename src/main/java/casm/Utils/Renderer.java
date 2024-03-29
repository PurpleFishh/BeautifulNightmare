package casm.Utils;

import casm.Game;
import casm.Scenes.SceneType;
import casm.Utils.Settings.Setting;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Used to display graphics on the screen
 */
public class Renderer {

    /**
     * Singleton instance
     */
    private static Renderer instance = null;

    private Renderer() {
    }

    /**
     * @return get the singleton instance
     */
    public static Renderer getInstance() {
        if (instance == null)
            instance = new Renderer();
        return instance;
    }

    /**
     * Draw an image on the screen
     *
     * @param image    the image to be drawn
     * @param position the position of the image
     * @param width    the width of the image
     * @param height   the height of the image
     */
    public void drawImage(BufferedImage image, Vector2D position, double width, double height) {
        if (position.x <= Setting.SCREEN_WIDTH + 64 && position.y <= Setting.SCREEN_HEIGHT + 64)
            return;
        Graphics g = Game.getGraphics();
        g.drawImage(image, (int) position.x, (int) position.y, (int) width, (int) height, null);

    }

    /**
     * Draw an image on the screen
     *
     * @param image           the image to be drawn
     * @param position        the position of the image
     * @param width           the width of the image
     * @param height          the height of the image
     * @param flip_vertical   flip the image vertically
     * @param flip_horizontal flip the image horizontally
     */
    public void drawImage(BufferedImage image, Vector2D position, double width, double height, boolean flip_vertical, boolean flip_horizontal) {
        Vector2D positionOnCamera = null;
        if (!(width == Setting.SCREEN_WIDTH && height == Setting.SCREEN_HEIGHT))
            positionOnCamera = calculateCameraPosition(position);
        else
            positionOnCamera = position;
        if (!(positionOnCamera.x <= Setting.SCREEN_WIDTH + 64 && positionOnCamera.y <= Setting.SCREEN_HEIGHT + 64))
            return;
        Graphics g = Game.getGraphics();
        int x_offset = 0, y_offset = 0;
        if (flip_horizontal) {
            x_offset = (int) width;
            width = -width;
        }
        if (flip_vertical) {
            y_offset = (int) height;
            height = -height;
        }

        g.drawImage(image, (int) Math.floor(positionOnCamera.x + x_offset), (int) Math.floor(positionOnCamera.y + y_offset), (int) width, (int) height, null);
    }

    /**
     * Draw a rectangle on the screen
     *
     * @param position the position of the rectangle
     * @param width    the width of the rectangle
     * @param height   the height of the rectangle
     * @param color    the color of the rectangle
     */
    public void drawRect(Vector2D position, double width, double height, Color color) {
        Vector2D positionOnCamera = calculateCameraPosition(position);
        if (!(positionOnCamera.x <= Setting.SCREEN_WIDTH + 64 && positionOnCamera.y <= Setting.SCREEN_HEIGHT + 64))
            return;
        Graphics g = Game.getGraphics();
        g.setColor(color);
        g.drawRect((int) positionOnCamera.x, (int) positionOnCamera.y, (int) width, (int) height);

    }

    /**
     * Calculate what is an object position after applying the camera offset
     *
     * @param position the position of the object
     * @return the position of the object after applying the camera offset
     */
    private Vector2D calculateCameraPosition(Vector2D position) {
        Vector2D positionOnCamera = (Vector2D) position.clone();
        if (Game.getCurrentScene().isPresent())
            if (Game.getCurrentScene().get().getType() == SceneType.LEVEL)
                if (Camera.hasTargget()) {
                    if (Camera.getInstance().equalsToTarget(position))
                        positionOnCamera.sub(Camera.getInstance().getCamera());
                    else {
                        positionOnCamera.x -= Camera.getInstance().getCamera().x - Camera.getInstance().getCameraOffset().x;
                        positionOnCamera.y -= Camera.getInstance().getCamera().y - Camera.getInstance().getCameraOffset().y;
                    }
                }

        return positionOnCamera;
    }

    /**
     * Display text on screen
     *
     * @param text     the text to be drawn
     * @param position the position of the text
     * @param font     the font of the text
     * @param color    the color of the text
     */
    public void drawString(String text, Vector2D position, Font font, Color color) {
        Vector2D positionOnCamera = calculateCameraPosition(position);
        if (!(positionOnCamera.x <= Setting.SCREEN_WIDTH + 64 && positionOnCamera.y <= Setting.SCREEN_HEIGHT + 64))
            return;
        Graphics g = Game.getGraphics();
        g.setColor(color);
        g.setFont(font);
        int height = g.getFontMetrics(font).getHeight();
        g.drawString(text, (int) position.x, (int) (position.y + height));

    }
}
