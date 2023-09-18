package casm.ECS.Components;

import casm.ECS.Component;
import casm.Game;
import casm.Utils.Renderer;
import casm.Utils.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A component used to create text
 * Dependencies: the entity needs to have {@link PositionComponent}
 */
public class TextComponent extends Component {
    /**
     * <b>text</b> - the text that will be rendered <br>
     * <b>fontName</b> - the font name <br>
     */
    private String text;
    private final String fontName;
    /**
     * The font size <br>
     */
    private int fontSize;
    /**
     * The color of the text <br>
     */
    private Color color;
    /**
     * The position of the text <br>
     */
    private Vector2D position;
    /**
     * The font used for the rendered text <br>
     */
    private Font font;

    /**
     * Create {@link TextComponent}
     * @param text the text that will be rendered
     * @param position the position of the text
     * @param fontName the font name
     * @param fontSize the font size
     * @param color the color of the text
     */
    public TextComponent(String text, Vector2D position, String fontName, int fontSize, Color color) {
        this.text = text;
        this.fontSize = fontSize;
        this.color = color;
        this.fontName = fontName;
        this.position = position;
        font = new Font(fontName, Font.PLAIN, fontSize);
    }

    /**
     * Draw the text on screen
     */
    @Override
    public void draw() {
        Renderer.getInstance().drawString(text, position, font, color);
    }

    /**
     *
     * @return the text that is dipalyed
     */
    public String getText() {
        return text;
    }

    /**
     * @return the size of the font
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * @return the color of the text
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param text the new text that will be displayed
     */
    public void setText(String text) {
        this.text = text;
        font = new Font(fontName, Font.PLAIN, fontSize);
    }

    /**
     * @param fontSize the new font size
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        font = new Font(fontName, Font.PLAIN, fontSize);
    }

    /**
     * @param color the new color of the text
     */
    public void setColor(Color color) {
        this.color = color;
        font = new Font(fontName, Font.PLAIN, fontSize);
    }

    /**
     * @param position the new position of the text
     */
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    /**
     * @return the position of the text
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * @return the size of the font
     */
    public Vector2D getSize()
    {
        Rectangle2D rect = Game.getGraphics().getFontMetrics(font).getStringBounds(text, Game.getGraphics());
        return new Vector2D(rect.getWidth(), rect.getHeight());
    }

    /**
     * @return the font used for the text
     */
    public Font getFont() {
        return font;
    }
}
