package com.team766.frc2020.paths;

/**
 * A waypoint along a path. Contains a position, radius (for creating curved
 * paths), and velocity. The information from these waypoints is used by the
 * PathBuilder class to generate paths.
 *
 */
public class Waypoint {

    // Translation2d position;
    private double x;
    private double y;
    private double curvature;
    // double radius;
    private double velocity;

    // used for calculating velocity and curvature
    // must be entered manually
    private double totalDistanceFromFirstWaypoint;

    public Waypoint() {};

    public Waypoint(double x, double y) {
        this.x = x;
        this.y = y;
        this.curvature = 0;
        this.velocity = 0;
    };

    public Waypoint(double x, double y, double velocity) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
    };

    public Waypoint(double x, double y, double curvature, double velocity, double totalDistanceFromFirstWaypoint) {
        this.x = x;
        this.y = y;
        this.curvature = curvature;
        this.velocity = velocity;
        this.totalDistanceFromFirstWaypoint = totalDistanceFromFirstWaypoint;
    }

    // returns new waypoint with the same variables as this waypoint
    public Waypoint clone(){
        return new Waypoint(this.getX(), this.getY(), this.getCurvature(), this.getVelocity(), this.getTotalDistanceFromFirstWaypoint());
    }

    
    public static double calculateDistanceBetweenTwoWaypoints(Waypoint pointA, Waypoint pointB) {
        return Math.sqrt(Math.pow(pointA.getX() - pointB.getX(), 2) + Math.pow(pointA.getY() - pointB.getY(), 2));
    }

    public Waypoint add(Waypoint a) {
        return new Waypoint(a.getX() + this.getX(), a.getY() + this.getY());
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

    public double getTotalDistanceFromFirstWaypoint() {
        return this.totalDistanceFromFirstWaypoint;
    }

    public void setTotalDistanceFromFirstWaypoint(double totalDistanceFromFirstWaypoint) {
        this.totalDistanceFromFirstWaypoint = totalDistanceFromFirstWaypoint;
    }

    public double getCurvature() {
        return this.curvature;
    }

    public void setCurvature(double curvature) {
        this.curvature = curvature;
    }

    public double getVelocity() {
        return this.velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}