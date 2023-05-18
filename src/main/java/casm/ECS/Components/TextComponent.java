package casm.ECS.Components;

import casm.ECS.Component;
import casm.Game;
import casm.Utils.Renderer;
import casm.Utils.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TextComponent extends Component {
    private String text, fontName;
    private int fontSize;
    private Color color;
    private Vector2D position;
    private Font font;

    public TextComponent(String text, Vector2D position, String fontName, int fontSize, Color color) {
        this.text = text;
        this.fontSize = fontSize;
        this.color = color;
        this.fontName = fontName;
        this.position = position;
        font = new Font(fontName, Font.PLAIN, fontSize);
    }

    @Override
    public void draw() {
        Renderer.getInstance().drawString(text, position, font, color);
    }

    public String getText() {
        return text;
    }

    public int getFontSize() {
        return fontSize;
    }

    public Color getColor() {
        return color;
    }

    public void setText(String text) {
        this.text = text;
        font = new Font(fontName, Font.PLAIN, fontSize);
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        font = new Font(fontName, Font.PLAIN, fontSize);
    }

    public void setColor(Color color) {
        this.color = color;
        font = new Font(fontName, Font.PLAIN, fontSize);
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getSize()
    {
        Rectangle2D rect = Game.getGraphics().getFontMetrics(font).getStringBounds(text, Game.getGraphics());
        return new Vector2D(rect.getWidth(), rect.getHeight());
    }

    public Font getFont() {
        return font;
    }
}
