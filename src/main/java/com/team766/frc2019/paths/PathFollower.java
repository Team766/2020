package com.team766.frc2019.paths;

import java.util.ArrayList;

import com.team766.frc2019.paths.Waypoint;
import com.team766.hal.RobotProvider;
import com.team766.controllers.PIDController;
import com.team766.frc2019.Robot;
import com.team766.frc2019.paths.Vector;

public class PathFollower {
    private ArrayList<Waypoint> path = new ArrayList<Waypoint>();
    private int previousLookaheadPointIndex = 0;
    private int lastClosestPointIndex = 0;
    private double heading = 0;
    private double xPosition = 0;
    private double yPosition = 0;
    private boolean isPathDone = false;
    PIDController m_turnController;




    public PathFollower(ArrayList<Waypoint> path) {
        this.path = path;
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
    }

    /**
     * @param xPosition x position of robot
     * @param yPosition y position of robot
     * @param lookaheadDistance radius of look ahead distance (values between 12 - 15 are good)
     */

    public void followPath(ArrayList<Waypoint> path) {
        
        m_turnController.setSetpoint(0.0);

        while(!isPathDone) {
            //System.out.println("position: " + Robot.drive.getXPosition() + ", " + Robot.drive.getYPosition());
            findLookaheadPoint(path, Robot.drive.getXPosition(), Robot.drive.getYPosition(), 13);

            m_turnController.calculate(calculateSteeringError(path, Robot.drive.getGyroAngle(), Robot.drive.getXPosition(), Robot.drive.getYPosition()), true);
            double turnPower = m_turnController.getOutput();
            double straightPower = path.get(previousLookaheadPointIndex).getVelocity();
            if (turnPower > 0) {
                Robot.drive.setDrive(straightPower, straightPower + turnPower);
            } else {
                Robot.drive.setDrive(straightPower - turnPower, straightPower);
            }

            if (turnPower > 0) {
                Robot.drive.setDrive(straightPower - turnPower, straightPower);
            } else {
                Robot.drive.setDrive(straightPower, straightPower + turnPower);
            }
            System.out.println("previousLookaheadPointIndex: " + previousLookaheadPointIndex + " Path size: " + path.size());
            if (previousLookaheadPointIndex == path.size() - 1) {
                isPathDone = true; 
                Robot.drive.setDrive(0, 0);
                System.out.println("path followed");
            } 
        }
        
    }

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

    public double calculateSteeringError(ArrayList<Waypoint> path, double heading, double xPosition, double yPosition){
        // based on angle
        Vector headingUnitVector = new Vector(Math.cos(heading), Math.sin(heading));
        Vector lookaheadVector = new Vector(path.get(previousLookaheadPointIndex).getX() - xPosition, path.get(previousLookaheadPointIndex).getY() - yPosition);
        return Math.acos((headingUnitVector.dot(lookaheadVector))/(0.000001 + headingUnitVector.magnitude() * lookaheadVector.magnitude()));
    }

    /**
     * calculates steering error with variables stored in PathFollower
     */
    // public double calculateSteeringError() {
    //     return calculateSteeringError(this.path, this.heading, this.xPosition, this.yPosition);
    // }

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
}
