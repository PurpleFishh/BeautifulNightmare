package casm.Utils;

import casm.SpriteUtils.Sprite;

import java.util.Objects;

public class Vector2D {

    public double x, y;

    public Vector2D() {
        x = 0;
        y = 0;
    }
    public Vector2D(Vector2D vec) {
        x = vec.x;
        y = vec.y;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(int w, int scr_w, double y) {
        this.x = (double) (scr_w / 2 - w / 2);
        this.y = y;
    }

    public Vector2D(double x, int h, int scr_h) {
        this.x = x;
        this.y = (double) (scr_h / 2 - h / 2);
    }

    public Vector2D(int w, int scr_w, int h, int scr_h) {
        this.x = (double) (scr_w / 2 + w / 2);
        this.y = (double) (scr_h / 2 + h / 2);
    }

    public Vector2D add(Vector2D vec) {
        x += vec.x;
        y += vec.y;
        return this;
    }

    public Vector2D sub(Vector2D vec) {
        x -= vec.x;
        y -= vec.y;
        return this;
    }

    public Vector2D mul(Vector2D vec) {
        x *= vec.x;
        y *= vec.y;
        return this;
    }

    public Vector2D div(Vector2D vec) {
        x /= vec.x;
        y /= vec.y;
        return this;
    }

    public Vector2D add(double val) {
        x += val;
        y += val;
        return this;
    }

    public Vector2D sub(double val) {
        x -= val;
        y -= val;
        return this;
    }

    public Vector2D mul(double val) {
        x *= val;
        y *= val;
        return this;
    }

    public Vector2D div(double val) {
        x /= val;
        y /= val;
        return this;
    }

    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D set(Vector2D vector) {
        this.x = vector.x;
        this.y = vector.y;

        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize() {
        double len = length();
        Vector2D aux = this;
        aux.x /= len;
        aux.y /= len;

        return aux;
    }

    @Override
    public Object clone() {
        return new Vector2D(this.x, this.y);
    }

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

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y;
    }

    public Vector2D centerVertically(int objectWidth, int width) {
        x += width / 2. - objectWidth / 2.;
        return this;
    }

    public Vector2D centerHorizontally(int objectHeight, int height) {
        y += height / 2. - objectHeight / 2.;
        return this;
    }
    public Vector2D centerVertically(double objectWidth, double width) {
        x += width / 2. - objectWidth / 2.;
        return this;
    }

    public Vector2D centerHorizontally(double objectHeight, double height) {
        y += height / 2. - objectHeight / 2.;
        return this;
    }

    public Vector2D center(int objectWidth, int objectHeight, int width, int height) {
        centerVertically(objectWidth, width);
        centerHorizontally(objectHeight, height);
        return this;
    } public Vector2D center(double objectWidth, double objectHeight, double width, double height) {
        centerVertically(objectWidth, width);
        centerHorizontally(objectHeight, height);
        return this;
    }
    public Vector2D center(Sprite objectSprite, int width, int height) {
        centerVertically(objectSprite.getWidth(), width);
        centerHorizontally(objectSprite.getHeight(), height);
        return this;
    }public Vector2D center(Sprite objectSprite, double width, double height) {
        centerVertically(objectSprite.getWidth(), width);
        centerHorizontally(objectSprite.getHeight(), height);
        return this;
    }
}
