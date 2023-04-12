package casm.Utils;

import java.util.Objects;

public class Vector2D {

    public double x, y;

    public Vector2D() {
        x = 0;
        y = 0;
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

    public void add(Vector2D vec) {
        x += vec.x;
        y += vec.y;
    }

    public void sub(Vector2D vec) {
        x -= vec.x;
        y -= vec.y;
    }

    public void mul(Vector2D vec) {
        x *= vec.x;
        y *= vec.y;
    }

    public void div(Vector2D vec) {
        x /= vec.x;
        y /= vec.y;
    }

    public void add(double val) {
        x += val;
        y += val;
    }

    public void sub(double val) {
        x -= val;
        y -= val;
    }

    public void mul(double val) {
        x *= val;
        y *= val;
    }

    public void div(double val) {
        x /= val;
        y /= val;
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
}
