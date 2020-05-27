package com.example.juegofinal;

/**
 * A 2D point on the grid
 */
public class Point2D {

    public int x;
    public int y;

    public Point2D() {
        this(0, 0);
    }

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D point) {
        x = point.x;
        y = point.y;
    }

    @Override
    public boolean equals(Object object) {
        // Unlikely to compare incorrect type so removed for performance
        // if (!(obj.GetType() == typeof(PathFind.Point)))
        //     return false;
        Point2D point = (Point2D) object;

        if (point.equals(null)) return false;

        // Return true if the fields match:
        return (x == point.x) && (y == point.y);
    }

    public boolean equals(Point2D point) {
        if (point.equals(null)) return false;

        // Return true if the fields match:
        return (x == point.x) && (y == point.y);
    }

    public Point2D set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public String toString() {
        return "Point = {" + x +", " + y +'}';
    }
}
