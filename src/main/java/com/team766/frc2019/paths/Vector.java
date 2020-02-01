package com.team766.frc2019.paths;

/**
 * 2d vector with functions useful for path building
 */
public class Vector {
    private double x;
    private double y;

    Vector(double x, double y) {
        this.setX(x);
        this.setY(y);
    }
    
    public double magnitude() {
        return(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    }

    public Vector normalize() {
        return(new Vector(x / magnitude(), y / magnitude()));
    }

    public Vector scale(double scalar) {
        return(new Vector(x * scalar, y * scalar));
    }
    
    public double dot(Vector a) {
        return a.getX() * this.getX() + a.getY() * this.getY();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}