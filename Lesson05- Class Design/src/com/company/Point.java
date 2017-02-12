package com.company;

/**
 * Created by Sapir Michaeli on 09.02.2017.
 */
public class Point {
    private int x,y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this) // אותו אוביקט בזיכרון באותה כתובת
            return true;
        if (obj instanceof Point) {
            Point other = (Point) obj;//downcast
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (7*x)^(11*y)^(53*y);
    }

}