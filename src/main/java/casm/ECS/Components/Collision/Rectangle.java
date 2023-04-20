package casm.ECS.Components.Collision;

import casm.Utils.Vector2D;

import java.awt.geom.Rectangle2D;

public class Rectangle extends Rectangle2D.Double {

    private Vector2D position, dimensions, offset = new Vector2D();

    public Rectangle(double x, double y, double w, double h) {
        super(x, y, w, h);
    }
    public Rectangle(double x, double y, double w, double h, double xOffset, double yOffset) {
        super(x, y, w, h);
        offset.set(xOffset, yOffset);
        position.add(offset);
        this.x += offset.x;
        this.y += offset.y;
    }

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

    public void setRect(Vector2D position, Vector2D dimensions) {
        super.setRect(position.x, position.y, dimensions.x, dimensions.y);
        this.position = (Vector2D) position.clone();
        this.position.add(offset);
        this.x += offset.x;
        this.y += offset.y;
        this.dimensions = (Vector2D) dimensions.clone();
    }

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

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getDimensions() {
        return dimensions;
    }

    public Vector2D getOffset() {
        return offset;
    }

    public void setOffset(Vector2D offset) {
        this.offset.set(offset);
    }
}
