package com.team766.frc2020.paths;

/**
 * 2d vector with functions useful for path building and path following
 */
public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
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

    /** 
     * can be negative or positive even though it says magnitude
     * vectors are crossed as if the z component is zero
     */
    public double crossMagnitudeSigned(Vector a) {
        return this.getX() * a.getY() - this.getY() * a.getX();
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