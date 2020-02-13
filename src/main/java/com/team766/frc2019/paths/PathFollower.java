package com.team766.frc2019.paths;

import java.util.ArrayList;

import com.team766.frc2019.paths.Waypoint;
import com.team766.frc2019.paths.Vector;

public class PathFollower {
    private ArrayList<Waypoint> path = new ArrayList<Waypoint>();
    private int previousLookaheadPointIndex = 0;
    private int lastClosestPointIndex = 0;
    private double heading = 0;
    private double xPosition = 0;
    private double yPosition = 0;
    public static boolean isPathDone = false;
    private Waypoint lookaheadWaypoint;
    private int index = 0;

    public PathFollower(ArrayList<Waypoint> path) {
        // TODO: add copy function for path
        this.path = path;
        // change this from magic number
        lookaheadWaypoint = findLookaheadPoint(13);
    }

    public void update() {
        setLastClosestPointIndex(findClosestPointIndex());
        setLookaheadWaypoint(findLookaheadPoint(13));
    }

    /**
     * finds lookahead point on path (connect the dots between path waypoints) based on lookahead radius
     * if no points are found then return the last lookahead point
     * @param xPosition x position of robot
     * @param yPosition y position of robot
     * @param lookaheadDistance radius of look ahead distance (values between 12 - 15 are good)
     */
    // TODO: add errors if path is length of zero
    public Waypoint findLookaheadPoint(ArrayList<Waypoint> path, double xPosition, double yPosition, double lookaheadDistance) {
        for (int i = getPreviousLookaheadPointIndex(); i < path.size() - 1; i++) {
            // https://stackoverflow.com/questions/1073336/circle-line-segment-collision-detection-algorithm/1084899#1084899
            Vector lineSegmentVector = new Vector(path.get(i + 1).getX() - path.get(i).getX(), path.get(i + 1).getY() - path.get(i).getY());
            Vector centerToRayStartVector = new Vector(
                path.get(i).getX() - xPosition,
                path.get(i).getY() - yPosition
            );
            // a b and c are from the quadratic formula
            double a = lineSegmentVector.dot(lineSegmentVector);
            double b = 2 * centerToRayStartVector.dot(lineSegmentVector);
            double c = centerToRayStartVector.dot(centerToRayStartVector) - Math.pow(lookaheadDistance, 2);
            double discriminant = Math.pow(b, 2) - 4 * a * c;
            if (discriminant < 0) {
                // no intersection
            } else {
                discriminant = Math.sqrt(discriminant);
                // t1 and t2 are the two values from the quadratic formula
                double t1 = (-b - discriminant) / (2 * a);
                double t2 = (-b + discriminant) / (2 * a);

                // Point = E + (t value of intersection) * d
                // if intersection exists find values
                if (index++ % 50 == 0) {
                    System.out.println("Lookahead point index: " + getPreviousLookaheadPointIndex() + " path size: " + path.size());
                }

                if (getPreviousLookaheadPointIndex() >= path.size()-5) {
                    System.out.println("Path is done yay");
                    isPathDone = true;
                }
                if (t1 >= 0 && t1 <=1) {
                    //return t1 intersection
                    setPreviousLookaheadPointIndex(i);
                    return path.get(i).add(new Waypoint(lineSegmentVector.getX() * t1, lineSegmentVector.getY() * t2));
                }
                if (t2 >= 0 && t2 <=1) {
                    //return t2 intersection
                    setPreviousLookaheadPointIndex(i);
                    return path.get(i).add(new Waypoint(lineSegmentVector.getX() * t2, lineSegmentVector.getY() * t2));
                }
            }
        }
        // otherwise, no intersection
        return path.get(getPreviousLookaheadPointIndex());
    }
    
    /**
     * finds lookahead point with variables stored in PathFollower
     */
    public Waypoint findLookaheadPoint(double lookaheadDistance) {
        return findLookaheadPoint(this.path, this.xPosition, this.yPosition, lookaheadDistance);
    }

    /**
     * finds closest point in PathFollower's path to (xPosition, yPosition)
     */
    public int findClosestPointIndex(ArrayList<Waypoint> path, double xPosition, double yPosition) {
        Waypoint position = new Waypoint(xPosition, yPosition);

        // set smallest distance to last known smallest point
        // and set smallest index to that point
        double smallestDistance = Waypoint.calculateDistanceBetweenTwoWaypoints(path.get(getLastClosestPointIndex()), position);
        int smallestIndex = getLastClosestPointIndex();

        // start at the point after the one we already calculated
        for (int i = lastClosestPointIndex + 1; i < path.size() - 1; i++) {
            double currentDistance = Waypoint.calculateDistanceBetweenTwoWaypoints(path.get(i), position);
            if (currentDistance < smallestDistance) {
                smallestDistance = currentDistance;
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    /**
     * finds closest point with variables stored in PathFollower
     */
    public int findClosestPointIndex() {
        return findClosestPointIndex(this.path, this.xPosition, this.yPosition);
    }

    /**
     * returns target velocity of closest point to (xPosition, yPosition) in path
     */
    public double findTargetVelocity(ArrayList<Waypoint> path, double xPosition, double yPosition) {
        return path.get(findClosestPointIndex(path, xPosition, yPosition)).getVelocity();
    }

    /**
     * finds target velocity using variables stored in PathFollower
     */
    public double findTargetVelocity() {
        return findTargetVelocity(this.path, this.xPosition, this.yPosition);
    }

    /**
     * calculates angle between vector between robot and lookahead point and
     * robot heading unit vector
     * @param path
     * @param heading
     * @param xPosition
     * @param yPosition
     * @return positive value robot needs to turn counterclockwise, negative if robot needs to turn clockwise
     */
    public double calculateSteeringError(ArrayList<Waypoint> path, double heading, double xPosition, double yPosition){
        Vector headingUnitVector = new Vector(Math.sin(Math.toRadians(heading)), Math.cos(Math.toRadians(heading)));
        Vector lookaheadVector = new Vector(this.lookaheadWaypoint.getX() - xPosition, this.lookaheadWaypoint.getY() - yPosition);
        double error =  Math.toDegrees(Math.acos(
            (headingUnitVector.dot(lookaheadVector)) /
            (0.000001 + headingUnitVector.magnitude() * lookaheadVector.magnitude())
        ));

        // make error negative if headingUnitVector is more counterclockwise than lookaheadVector
        if (headingUnitVector.crossMagnitude(lookaheadVector) < 0) {
            return error * -1;
        } else {
            return error;
        }
    }

    public boolean isPathDone() {
        return isPathDone;
    }

    /**
     * calculates steering error with variables stored in PathFollower
     */
    public double calculateSteeringError() {
        return calculateSteeringError(this.path, this.heading, this.xPosition, this.yPosition);
    }

    public int getPreviousLookaheadPointIndex() {
        return this.previousLookaheadPointIndex;
    }

    public void setPreviousLookaheadPointIndex(int previousLookaheadPointIndex) {
        this.previousLookaheadPointIndex = previousLookaheadPointIndex;
    }

    public ArrayList<Waypoint> getPath() {
        return this.path;
    }

    public void setPath(ArrayList<Waypoint> path) {
        this.path = path;
    }

    public int getLastClosestPointIndex() {
        return this.lastClosestPointIndex;
    }

    public void setLastClosestPointIndex(int lastClosestPointIndex) {
        this.lastClosestPointIndex = lastClosestPointIndex;
    }

    public void setPosition(double xPosition, double yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public Waypoint getLookaheadWaypoint() {
        return this.lookaheadWaypoint;
    }
    
    public void setLookaheadWaypoint(Waypoint lookaheadWaypoint) {
        this.lookaheadWaypoint = lookaheadWaypoint;
    }
}
