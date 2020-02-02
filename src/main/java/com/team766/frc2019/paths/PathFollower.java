package com.team766.frc2019.paths;

import java.util.ArrayList;

import com.team766.frc2019.paths.Waypoint;
import com.team766.frc2019.paths.Vector;

public class PathFollower {
    private ArrayList<Waypoint> path = new ArrayList<Waypoint>();
    private int previousLookaheadPointIndex = 0;
    private int lastClosestPointIndex = 0;

    public PathFollower(ArrayList<Waypoint> path) {
        this.path = path;
    }

    /**
     * @param lookaheadDistance radius of look ahead distance (values between 12 - 15 are good)
     */
    public Waypoint findLookaheadPoint(double xPosition, double yPosition, double lookaheadDistance) {
        for (int i = getPreviousLookaheadPointIndex(); i < getPath().size() - 1; i++) {
            Vector lineSegmentVector = new Vector(getPath().get(i + 1).getX() - getPath().get(i).getX(), getPath().get(i + 1).getY() - getPath().get(i).getY());
            Vector centerToRayStartVector = new Vector(
                getPath().get(i).getX() - xPosition,
                getPath().get(i).getY() - yPosition
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
                if (t1 >= 0 && t1 <=1) {
                    //return t1 intersection
                    setPreviousLookaheadPointIndex(i);
                    return getPath().get(i).add(new Waypoint(lineSegmentVector.getX() * t1, lineSegmentVector.getY() * t2));
                }
                if (t2 >= 0 && t2 <=1) {
                    //return t2 intersection
                    setPreviousLookaheadPointIndex(i);
                    return getPath().get(i).add(new Waypoint(lineSegmentVector.getX() * t2, lineSegmentVector.getY() * t2));
                }
            }
        }
        // otherwise, no intersection
        return getPath().get(getPreviousLookaheadPointIndex());
    }

    public int findClosestPointIndex(double xPosition, double yPosition) {
        Waypoint position = new Waypoint(xPosition, yPosition);

        // set smallest distance to last known smallest point
        // and set smallest index to that point
        double smallestDistance = Waypoint.calculateDistanceBetweenTwoWaypoints(path.get(getLastClosestPointIndex()), position);
        int smallestIndex = getLastClosestPointIndex();

        // start at the point after the one we already calculated
        for (int i = lastClosestPointIndex + 1; i < getPath().size() - 1; i++) {
            double currentDistance = Waypoint.calculateDistanceBetweenTwoWaypoints(path.get(i), position);
            if (currentDistance < smallestDistance) {
                smallestDistance = currentDistance;
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    public double findTargetVelocity(double xPosition, double yPosition) {
        return getPath().get(findClosestPointIndex(xPosition, yPosition)).getVelocity();
    }

    public double calculateSteeringError(double heading, double xPosition, double yPosition){
        // based on angle
        Vector headingUnitVector = new Vector(Math.sin(heading), Math.cos(heading));
        Vector lookaheadVector = new Vector(getPath().get(previousLookaheadPointIndex).getX() - xPosition, getPath().get(previousLookaheadPointIndex).getY() - yPosition);
        return Math.acos((headingUnitVector.dot(lookaheadVector))/(headingUnitVector.magnitude() * lookaheadVector.magnitude()));
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
}
