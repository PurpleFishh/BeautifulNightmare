package casm.Utils;

import casm.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {
    public static void drawImage(BufferedImage image, Vector2D position, double width, double height) {
        Graphics g = Game.getGraphics();

        g.drawImage(image, (int) position.x, (int) position.y, (int) width, (int) height, null);
    }

    public static void drawImage(BufferedImage image, Vector2D position, double width, double height, boolean flip_vertical, boolean flip_horizontal) {
        Graphics g = Game.getGraphics();

        Vector2D positionOnCamera = (Vector2D) position.clone();
        if (Camera.hasTargget()) {
            if (Camera.getInstance().equalsToTarget(position))
                positionOnCamera.sub(Camera.getInstance().getCamera());
            else {
                positionOnCamera.x -= Camera.getInstance().getCamera().x + Camera.getInstance().getCameraOffset().x;
                positionOnCamera.y -= Camera.getInstance().getCamera().y + Camera.getInstance().getCameraOffset().y;
            }
        }

        int x_offset = 0, y_offset = 0;
        if (flip_horizontal) {
            y_offset = (int) height;
            height = -height;
        }
        if (flip_vertical) {
            x_offset = (int) width;
            width = -width;
        }

        g.drawImage(image, (int) (positionOnCamera.x + x_offset), (int) (positionOnCamera.y + y_offset), (int) width, (int) height, null);
    }

    public static void drawRect(Vector2D position, double width, double height) {
        Graphics g = Game.getGraphics();

        g.setColor(Color.RED);
        Vector2D positionOnCamera = (Vector2D) position.clone();
        if (Camera.hasTargget()) {
            if (Camera.getInstance().equalsToTarget(position)) {
                positionOnCamera.sub(Camera.getInstance().getCamera());
            } else {
                positionOnCamera.x -= Camera.getInstance().getCamera().x + Camera.getInstance().getCameraOffset().x;
                positionOnCamera.y -= Camera.getInstance().getCamera().y + Camera.getInstance().getCameraOffset().y;
            }
        }

        g.drawRect((int) positionOnCamera.x, (int) positionOnCamera.y, (int) width, (int) height);
    }
}