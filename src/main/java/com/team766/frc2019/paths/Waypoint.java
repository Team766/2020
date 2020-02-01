package com.team766.frc2019.paths;

/**
 * A waypoint along a path. Contains a position, radius (for creating curved
 * paths), and velocity. The information from these waypoints is used by the
 * PathBuilder class to generate Paths. Waypoints also contain an optional
 * marker that is used by the WaitForPathMarkerAction.
 *
 * @see GeneratePath
 */
public class Waypoint {
    // Translation2d position;
    private double x;
    private double y;
    private double curvature;
    // double radius;
    // double velocity;

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

    public Waypoint clone(){
        Waypoint waypoint = new Waypoint();

        waypoint.setX(this.getX());
        waypoint.setY(this.getY());
        waypoint.setTotalDistanceFromFirstWaypoint(this.getTotalDistanceFromFirstWaypoint());
        waypoint.setCurvature(this.getCurvature());
        waypoint.setVelocity(this.getVelocity());
        return waypoint;
    }

    public static double calculateDistanceBetweenTwoWaypoints(Waypoint pointA, Waypoint pointB) {
        return Math.sqrt(Math.pow(pointA.getX() - pointB.getX(), 2) + Math.pow(pointA.getY() - pointB.getY(), 2));
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