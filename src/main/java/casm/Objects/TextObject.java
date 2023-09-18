package casm.Objects;

import casm.ECS.Components.TextComponent;
import casm.Game;
import casm.Utils.Vector2D;

import java.awt.*;

/**
 * Object used to display text on the screen.
 */
public class TextObject extends Object {

    /**
     * <b>width</b> - The width of the text.
     * <b>height</b> - The height of the text.
     */
    private int width, height;

    /**
     * @param text          - The text to be displayed.
     * @param spawnPosition - The position where the text will be displayed.
     */
    public TextObject(String text, Vector2D spawnPosition) {
        super("Text Object", spawnPosition);
        initialize(text, spawnPosition, "Eras Bold ITC", 20, new Color(219, 246, 255));
    }

    /**
     * @param text          - The text to be displayed.
     * @param spawnPosition - The position where the text will be displayed.
     * @param fontSize      - The size of the font.
     * @param color         - The color of the text.
     */
    public TextObject(String text, Vector2D spawnPosition, int fontSize, Color color) {
        super("Text Object", spawnPosition);
        initialize(text, spawnPosition, "Eras Bold ITC", fontSize, color);
    }

    /**
     * @param text          - The text to be displayed.
     * @param spawnPosition - The position where the text will be displayed.
     * @param fontSize      - The size of the font.
     */
    public TextObject(String text, Vector2D spawnPosition, int fontSize) {
        super("Text Object", spawnPosition);
        initialize(text, spawnPosition, "Eras Bold ITC", fontSize, new Color(219, 246, 255));
    }

    /**
     * @param text          - The text to be displayed.
     * @param spawnPosition - The position where the text will be displayed.
     * @param fontName      - The name of the font.
     * @param fontSize      - The size of the font.
     */
    public TextObject(String text, Vector2D spawnPosition, String fontName, int fontSize) {
        super("Text Object", spawnPosition);
        initialize(text, spawnPosition, fontName, fontSize, new Color(219, 246, 255));
    }

    /**
     * Initializes the text object.
     *
     * @param text          - The text to be displayed.
     * @param spawnPosition - The position where the text will be displayed.
     * @param fontName      - The name of the font.
     * @param fontSize      - The size of the font.
     * @param color         - The color of the text.
     */
    public void initialize(String text, Vector2D spawnPosition, String fontName, int fontSize, Color color) {
        this.addComponent(new TextComponent(text, spawnPosition, fontName, fontSize, color));
        Graphics g = Game.getGraphics();
        Font font = this.getComponent(TextComponent.class).getFont();
        height = g.getFontMetrics(font).getHeight();
        width = g.getFontMetrics(font).stringWidth(text);
    }

    /**
     * Centers the text object to a rectangle area.
     *
     * @param centerTo       - The start position of the area.
     * @param centerToWidth  - The width of the area.
     * @param centerToHeight - The height of the area.
     * @return - Reference to the object.
     */
    public TextObject center(Vector2D centerTo, double centerToWidth, double centerToHeight) {
        Vector2D size = this.getComponent(TextComponent.class).getSize();
        this.getComponent(TextComponent.class).setPosition(new Vector2D(centerTo).center(size.x, size.y,
                centerToWidth, centerToHeight - size.y / 2));
        return this;
    }

    /**
     * @param color - The color of the text.
     * @return - Reference to the object.
     */
    public TextObject setColor(Color color) {
        this.getComponent(TextComponent.class).setColor(color);
        return this;
    }

    /**
     * @param size - The size of the font.
     * @return - Reference to the object.
     */
    public TextObject setFontSize(int size) {
        this.getComponent(TextComponent.class).setFontSize(size);
        return this;
    }

    /**
     * @param text - The text to be displayed.
     * @return - Reference to the object.
     */
    public TextObject setText(String text) {
        this.getComponent(TextComponent.class).setText(text);
        Graphics g = Game.getGraphics();
        Font font = this.getComponent(TextComponent.class).getFont();
        height = g.getFontMetrics(font).getHeight();
        width = g.getFontMetrics(font).stringWidth(text);
        return this;
    }

    /**
     * @return - The text to be displayed.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @return - The text to be displayed.
     */
    @Override
    public int getHeight() {
        return height;
    }
}
