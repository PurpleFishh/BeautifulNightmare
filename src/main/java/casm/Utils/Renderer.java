package casm.Utils;

import casm.Game;
import casm.Utils.Settings.Setting;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {

    private static Renderer instance = null;

    private Renderer() {
    }

    public static Renderer getInstance() {
        if (instance == null)
            instance = new Renderer();
        return instance;
    }

    public void drawImage(BufferedImage image, Vector2D position, double width, double height) {
        if (position.x <= Setting.SCREEN_WIDTH + 64 && position.y <= Setting.SCREEN_HEIGHT + 64) {
            Graphics g = Game.getGraphics();

            g.drawImage(image, (int) position.x, (int) position.y, (int) width, (int) height, null);
        }
    }

    public void drawImage(BufferedImage image, Vector2D position, double width, double height, boolean flip_vertical, boolean flip_horizontal) {
        Vector2D positionOnCamera = calculateCameraPosition(position);
        if (positionOnCamera.x <= Setting.SCREEN_WIDTH + 64 && positionOnCamera.y <= Setting.SCREEN_HEIGHT + 64) {
            Graphics g = Game.getGraphics();
            int x_offset = 0, y_offset = 0;
            if (flip_horizontal) {
                y_offset = (int) height;
                height = -height;
            }
            if (flip_vertical) {
                x_offset = (int) width;
                width = -width;
            }

            g.drawImage(image, (int) Math.floor(positionOnCamera.x + x_offset), (int) Math.floor(positionOnCamera.y + y_offset), (int) width, (int) height, null);
        }
    }

    public void drawRect(Vector2D position, double width, double height, Color color) {
        Vector2D positionOnCamera = calculateCameraPosition(position);
        if (positionOnCamera.x <= Setting.SCREEN_WIDTH + 64 && positionOnCamera.y <= Setting.SCREEN_HEIGHT + 64) {
            Graphics g = Game.getGraphics();
            g.setColor(color);
            g.drawRect((int) positionOnCamera.x, (int) positionOnCamera.y, (int) width, (int) height);
        }
    }

    private Vector2D calculateCameraPosition(Vector2D position) {
        Vector2D positionOnCamera = (Vector2D) position.clone();
        if (Camera.hasTargget()) {
            if (Camera.getInstance().equalsToTarget(position)) {
                positionOnCamera.sub(Camera.getInstance().getCamera());
            } else {
                positionOnCamera.x -= Camera.getInstance().getCamera().x - Camera.getInstance().getCameraOffset().x;
                positionOnCamera.y -= Camera.getInstance().getCamera().y - Camera.getInstance().getCameraOffset().y;
            }
        }
        return positionOnCamera;
    }
}
