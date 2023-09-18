package casm.ECS.Components.Collision;

import casm.Utils.Vector2D;

import java.awt.geom.Rectangle2D;

/**
 * This is the class used for making rectangles usually for colliders
 * It extends the {@link Rectangle2D} class adding {@link Vector2D} details, and offset with more functionality
 */
public class Rectangle extends Rectangle2D.Double {

    /**
     * position - the position of the rectangle stored as {@link Vector2D}<br>
     * dimensions - the dimensions of the rectangle stored as {@link Vector2D}<br>
     * offset - the offset of the rectangle, the rectangle will be at that offset of the position
     */
    private Vector2D position;
    private Vector2D dimensions;
    private final Vector2D offset = new Vector2D();

    /**
     * Constructing a new rectangle
     *
     * @param x x position of the rectangle
     * @param y y position of the rectangle
     * @param w with of the rectangle
     * @param h height of the rectangle
     */
    public Rectangle(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    /**
     * Constructing a new rectangle
     *
     * @param x       x position of the rectangle
     * @param y       y position of the rectangle
     * @param w       with of the rectangle
     * @param h       height of the rectangle
     * @param xOffset x of the offset that will be applied to the position
     * @param yOffset y of the offset that will be applied to the position
     */
    public Rectangle(double x, double y, double w, double h, double xOffset, double yOffset) {
        super(x, y, w, h);
        offset.set(xOffset, yOffset);
        position.add(offset);
        this.x += offset.x;
        this.y += offset.y;
    }

    /**
     * Set new parameters for the rectangle
     *
     * @param x x position of the rectangle
     * @param y y position of the rectangle
     * @param w with of the rectangle
     * @param h height of the rectangle
     */
    @Override
    public void setRect(double x, double y, double w, double h) {
        super.setRect(x, y, w, h);
        if (position == null)
            position = new Vector2D(x, y);
        else {
            position.set(x, y);
            position.add(offset);
            this.x += offset.x;
            this.y += offset.y;
        }
        if (dimensions == null)
            dimensions = new Vector2D(w, h);
        else
            dimensions.set(w, h);
    }

    /**
     * Set new parameters for the rectangle
     *
     * @param position   position of the rectangle
     * @param dimensions dimensions of the rectangle
     */
    public void setRect(Vector2D position, Vector2D dimensions) {
        super.setRect(position.x, position.y, dimensions.x, dimensions.y);
        this.position = (Vector2D) position.clone();
        this.position.add(offset);
        this.x += offset.x;
        this.y += offset.y;
        this.dimensions = (Vector2D) dimensions.clone();
    }

    /**
     * Set new parameters for the rectangle
     *
     * @param r a rectangle that will be copied the information from
     */
    @Override
    public void setRect(Rectangle2D r) {
        super.setRect(r);
        if (position == null)
            position = new Vector2D(r.getX(), r.getY());
        else
            position.set(x, y);
        position.add(offset);
        this.x += offset.x;
        this.y += offset.y;
        if (dimensions == null)
            dimensions = new Vector2D(r.getWidth(), r.getHeight());
        else
            dimensions.set(r.getWidth(), r.getHeight());
    }
    /**
     * Set new parameters for the rectangle
     *
     * @param r a rectangle that will be copied the information from
     */
    public void setRect(Rectangle r) {
        super.setRect(r);
        if (position == null)
            position = new Vector2D(r.getX(), r.getY());
        else
            position.set(x, y);
        position.add(offset);
        this.x += offset.x;
        this.y += offset.y;
        if (dimensions == null)
            dimensions = new Vector2D(r.getWidth(), r.getHeight());
        else
            dimensions.set(r.getWidth(), r.getHeight());
    }

    /**
     * @return get rectangle position
     */
    public Vector2D getPosition() {
        return position;
    }
    /**
     * @return get rectangle dimensions
     */
    public Vector2D getDimensions() {
        return dimensions;
    }
    /**
     * @return get rectangle offset
     */
    public Vector2D getOffset() {
        return offset;
    }
    /**
     * Set rectangle offset
     * @param offset new rectangle offset
     */
    public void setOffset(Vector2D offset) {
        this.offset.set(offset);
    }
}
