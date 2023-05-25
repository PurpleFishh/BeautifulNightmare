package casm.Utils;

import casm.SpriteUtils.Sprite;

import java.util.Objects;

/**
 * Used to store 2D coordinates
 */
public class Vector2D {

    /**
     * <b>x</b> - x coordinate
     * <b>y</b> - y coordinate
     */
    public double x, y;

    /**
     * Create a new Vector2D with x and y set to 0
     */
    public Vector2D() {
        x = 0;
        y = 0;
    }

    /**
     * Create a new Vector2D that will be a copy of the given Vector2D
     *
     * @param vec - Vector2D to copy
     */
    public Vector2D(Vector2D vec) {
        x = vec.x;
        y = vec.y;
    }

    /**
     * Create a new Vector2D with the given coordinates
     *
     * @param x - x coordinate
     * @param y - y coordinate
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a vector centered horizontally
     *
     * @param w     object width
     * @param scr_w width of the object that it will be centered
     * @param y     y coordinate
     */
    public Vector2D(int w, int scr_w, double y) {
        this.x = (double) (scr_w / 2 - w / 2);
        this.y = y;
    }

    /**
     * Create a vector centered vertically
     *
     * @param h     object height
     * @param scr_h height of the object that it will be centered
     * @param x     x coordinate
     */
    public Vector2D(double x, int h, int scr_h) {
        this.x = x;
        this.y = (double) (scr_h / 2 - h / 2);
    }

    /**
     * Create a vector centered horizontally and vertically
     *
     * @param w     object width
     * @param scr_w width of the object that it will be centered
     * @param h     object height
     * @param scr_h height of the object that it will be centered
     */
    public Vector2D(int w, int scr_w, int h, int scr_h) {
        this.x = (double) (scr_w / 2 + w / 2);
        this.y = (double) (scr_h / 2 + h / 2);
    }

    /**
     * Add to the vector x and y the given vector x and y
     *
     * @param vec vector to be added
     * @return reference to the vector after the operation
     */
    public Vector2D add(Vector2D vec) {
        x += vec.x;
        y += vec.y;
        return this;
    }

    /**
     * Subtract from the vector x and y the given vector x and y
     *
     * @param vec vector to be subtracted
     * @return reference to the vector after the operation
     */
    public Vector2D sub(Vector2D vec) {
        x -= vec.x;
        y -= vec.y;
        return this;
    }

    /**
     * Multiply the vector x and y with the given vector x and y
     *
     * @param vec vector to be multiplied
     * @return reference to the vector after the operation
     */
    public Vector2D mul(Vector2D vec) {
        x *= vec.x;
        y *= vec.y;
        return this;
    }

    /**
     * Divide the vector x and y with the given vector x and y
     *
     * @param vec vector to be divided
     * @return reference to the vector after the operation
     */
    public Vector2D div(Vector2D vec) {
        x /= vec.x;
        y /= vec.y;
        return this;
    }

    /**
     * Add a value to vector x and y
     *
     * @param val value to be added
     * @return reference to the vector after the operation
     */
    public Vector2D add(double val) {
        x += val;
        y += val;
        return this;
    }

    /**
     * Subtract a value from vector x and y
     *
     * @param val value to be subtracted
     * @return reference to the vector after the operation
     */
    public Vector2D sub(double val) {
        x -= val;
        y -= val;
        return this;
    }

    /**
     * Multiply a value with vector x and y
     *
     * @param val value to be multiplied
     * @return reference to the vector after the operation
     */
    public Vector2D mul(double val) {
        x *= val;
        y *= val;
        return this;
    }

    /**
     * Divide a value from vector x and y
     *
     * @param val value to be divided
     * @return reference to the vector after the operation
     */
    public Vector2D div(double val) {
        x /= val;
        y /= val;
        return this;
    }

    /**
     * Set new coordinates to the vector
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return reference to the vector after the operation
     */
    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Set new coordinates to the vector form another vector that will be copied
     *
     * @param vector vector to be copied
     * @return reference to the vector after the operation
     */
    public Vector2D set(Vector2D vector) {
        this.x = vector.x;
        this.y = vector.y;

        return this;
    }

    /**
     * Math length of the vector
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalization of the vector
     *
     * @return normalized vector
     */
    public Vector2D normalize() {
        double len = length();
        Vector2D aux = this;
        aux.x /= len;
        aux.y /= len;

        return aux;
    }

    /**
     * Used to make a copy of the vector
     *
     * @return the cloned vector
     */
    @Override
    public Object clone() {
        return new Vector2D(this.x, this.y);
    }

    /**
     * If two vectors are equal
     *
     * @param o object to be compared
     * @return true if the vectors are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return Double.compare(vector2D.x, x) == 0 && Double.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Serialize the vector
     *
     * @return the vector as a string
     */
    @Override
    public String toString() {
        return "x = " + x + ", y = " + y;
    }

    /**
     * Center the vector vertically to an object
     *
     * @param objectWidth width of the object
     * @param width       the width of the object that it will be centered
     * @return reference to the vector after the operation
     */
    public Vector2D centerVertically(int objectWidth, int width) {
        x += width / 2. - objectWidth / 2.;
        return this;
    }

    /**
     * Center the vector horizontally to an object
     *
     * @param objectHeight height of the object
     * @param height       the height of the object that it will be centered
     * @return reference to the vector after the operation
     */
    public Vector2D centerHorizontally(int objectHeight, int height) {
        y += height / 2. - objectHeight / 2.;
        return this;
    }

    /**
     * Center the vector vertically to an object
     *
     * @param objectWidth width of the object
     * @param width       the width of the object that it will be centered
     * @return reference to the vector after the operation
     */
    public Vector2D centerVertically(double objectWidth, double width) {
        x += width / 2. - objectWidth / 2.;
        return this;
    }

    /**
     * Center the vector horizontally to an object
     *
     * @param objectHeight height of the object
     * @param height       the height of the object that it will be centered
     * @return reference to the vector after the operation
     */
    public Vector2D centerHorizontally(double objectHeight, double height) {
        y += height / 2. - objectHeight / 2.;
        return this;
    }

    /**
     * Center the vector vertically and horizontally to an object
     *
     * @param objectWidth  width of the object
     * @param objectHeight height of the object
     * @param width        width of the object that it will be centered
     * @param height       height of the object that it will be centered
     * @return reference to the vector after the operation
     */
    public Vector2D center(int objectWidth, int objectHeight, int width, int height) {
        centerVertically(objectWidth, width);
        centerHorizontally(objectHeight, height);
        return this;
    }

    /**
     * Center the vector vertically and horizontally to an object
     *
     * @param objectWidth  width of the object
     * @param objectHeight height of the object
     * @param width        width of the object that it will be centered
     * @param height       height of the object that it will be centered
     * @return reference to the vector after the operation
     */
    public Vector2D center(double objectWidth, double objectHeight, double width, double height) {
        centerVertically(objectWidth, width);
        centerHorizontally(objectHeight, height);
        return this;
    }

    /**
     * Center the vector vertically and horizontally to an object
     *
     * @param objectSprite sprite of the object
     * @param width        width of the object that it will be centered
     * @param height       height of the object that it will be centered
     * @return reference to the vector after the operation
     */
    public Vector2D center(Sprite objectSprite, int width, int height) {
        centerVertically(objectSprite.getWidth(), width);
        centerHorizontally(objectSprite.getHeight(), height);
        return this;
    }

    /**
     * Center the vector vertically and horizontally to an object
     *
     * @param objectSprite sprite of the object
     * @param width        width of the object that it will be centered
     * @param height       height of the object that it will be centered
     * @return reference to the vector after the operation
     */
    public Vector2D center(Sprite objectSprite, double width, double height) {
        centerVertically(objectSprite.getWidth(), width);
        centerHorizontally(objectSprite.getHeight(), height);
        return this;
    }
}
