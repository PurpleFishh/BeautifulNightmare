package casm.Objects;

import casm.ECS.Components.TextComponent;
import casm.Game;
import casm.Scenes.SettingsMenuScene;
import casm.Utils.Vector2D;

import java.awt.*;

public class TextObject extends Object {

    private int width, height;

    public TextObject(String text, Vector2D spawnPosition) {
        super("Text Object", spawnPosition);
        initialize(text, spawnPosition, "Eras Bold ITC", 20, new Color(219, 246, 255));
    }

    public TextObject(String text, Vector2D spawnPosition, int fontSize, Color color) {
        super("Text Object", spawnPosition);
        initialize(text, spawnPosition, "Eras Bold ITC", fontSize, color);
    }

    public TextObject(String text, Vector2D spawnPosition, int fontSize) {
        super("Text Object", spawnPosition);
        initialize(text, spawnPosition, "Eras Bold ITC", fontSize, new Color(219, 246, 255));
    }

    public TextObject(String text, Vector2D spawnPosition, String fontName, int fontSize) {
        super("Text Object", spawnPosition);
        initialize(text, spawnPosition, fontName, fontSize, new Color(219, 246, 255));
    }

    public void initialize(String text, Vector2D spawnPosition, String fontName, int fontSize, Color color) {
        this.addComponent(new TextComponent(text, spawnPosition, fontName, fontSize, color));
        Graphics g = Game.getGraphics();
        Font font = this.getComponent(TextComponent.class).getFont();
        height = g.getFontMetrics(font).getHeight();
        width = g.getFontMetrics(font).stringWidth(text);
    }

    public TextObject center(Vector2D centerTo, double centerToWidth, double centerToHeight) {
        Vector2D size = this.getComponent(TextComponent.class).getSize();
        this.getComponent(TextComponent.class).setPosition(new Vector2D(centerTo).center(size.x, size.y,
                centerToWidth, centerToHeight - size.y / 2));
        return this;
    }

    public TextObject setColor(Color color) {
        this.getComponent(TextComponent.class).setColor(color);
        return this;
    }

    public TextObject setFontSize(int size) {
        this.getComponent(TextComponent.class).setFontSize(size);
        return this;
    }

    public TextObject setText(String text) {
        this.getComponent(TextComponent.class).setText(text);
        Graphics g = Game.getGraphics();
        Font font = this.getComponent(TextComponent.class).getFont();
        height = g.getFontMetrics(font).getHeight();
        width = g.getFontMetrics(font).stringWidth(text);
        return this;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
