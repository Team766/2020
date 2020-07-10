package com.team766.frc2020.paths;

import java.util.ArrayList;

import com.team766.frc2020.paths.Waypoint;
import com.team766.frc2020.paths.Vector;

// TODO: make methods static idk why maybe its good
public class PathFollower {

    private final float maxAcceleration = 10;

    private ArrayList<Waypoint> path = new ArrayList<Waypoint>();
    private int previousLookaheadPointIndex = 0;
    private int lastClosestPointIndex = 0;
    private double heading = 0;
    private double xPosition = 0;
    private double yPosition = 0;
    // private static boolean isPathDone = false;
    private Waypoint lookaheadWaypoint;
    private int index = 0;
    private double lookaheadDistance = 13;
    private boolean inverted = false;

    private double targetSpeed = 0;
    private double targetAcceleration = 0;
    // TODO: get trackwidth and maybe kV and kA from config file or drive so it is not stored in path follower
    // width of robot + a few inches (measured in inches);
    private final double trackWidth = 30;
    // used for feedforward
    private final double kV = 0.001;
    private final double kA = 0.001;
    // left/right side drive target velocity
    private double leftTargetVelocity = 0;
    private double rightTargetVelocity = 0;
    // feedforward constant
    private double feedforward;
    private double previousTime = 0;
    private double deltaTime = 0;

    public PathFollower(ArrayList<Waypoint> path) {
        // TODO: add copy function for path
        this.path = path;

        lookaheadWaypoint = findLookaheadPoint(this.lookaheadDistance);
    }

    /**
     * update should be called after setting new position and heading
     * does several calculations to keep variables up to date
     */
    public void update() {
        setLastClosestPointIndex(findClosestPointIndex());
        setLookaheadWaypoint(findLookaheadPoint(this.lookaheadDistance));
        double previousTargetVelocity = targetSpeed;
        setTargetSpeed(findTargetSpeed());

        targetAcceleration = (targetSpeed - previousTargetVelocity) / deltaTime;
        
        setFeedforward(calculateFeedforward());

        double[] leftRightTargetVelocity = calculateLeftAndRightTargetVelocities();
        setLeftTargetVelocity(leftRightTargetVelocity[0]);
        setRightTargetVelocity(leftRightTargetVelocity[1]);

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
        // loop through points to find intersection starting with last lookahead point
        for (int i = getPreviousLookaheadPointIndex(); i < path.size() - 1; i++) {
            // line segment circle intersection algorithm
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
                // TODO: check if the lookahead circle interects two points between two waypoints it pick the one farthest along the path
                if (index++ % 50 == 0) {
                    // System.out.println("Lookahead point index: " + getPreviousLookaheadPointIndex() + " path size: " + path.size());
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
        for (int i = getLastClosestPointIndex() + 1; i < path.size() - 1; i++) {
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
    public double findTargetSpeed(double targetSpeed, ArrayList<Waypoint> path, double xPosition, double yPosition, double maxAcceleration, double deltaTime) {
        // rate limit
        double maxChange = deltaTime * maxAcceleration;
        // velocity at closest point
        double change = path.get(findClosestPointIndex(path, xPosition, yPosition)).getVelocity() - targetSpeed;
        
        change = (change > maxChange) ? maxChange : change;
        change = (change < -maxChange) ? -maxChange : change;

        return targetSpeed + change;
    }

    /**
     * finds target velocity using variables stored in PathFollower
     */
    public double findTargetSpeed() {
        return findTargetSpeed(this.targetSpeed, this.path, this.xPosition, this.yPosition, this.maxAcceleration, this.deltaTime);
    }


    /**
     * calculates distance from lookahead point to robot heading line that is signed for the side the robot is on
     * compared to the path
     * @param path
     * @param heading
     * @param xPosition
     * @param yPosition
     * @return positive value robot needs to turn counterclockwise, negative if robot needs to turn clockwise
     */
    public double calculateSignedDistanceFromLookaheadPointToRobotHeadingLine(ArrayList<Waypoint> path, double heading, double xPosition, double yPosition) {
        // calculate angle between lookahead vector and heading vector
        Vector headingUnitVector = new Vector(Math.sin(Math.toRadians(heading)), Math.cos(Math.toRadians(heading)));
        Vector lookaheadVector = new Vector(this.lookaheadWaypoint.getX() - xPosition, this.lookaheadWaypoint.getY() - yPosition);

        // contstant in line equation
        double c = headingUnitVector.getX() * xPosition + headingUnitVector.getY() * yPosition;
        // refered to as "x" or "X" in 1712 whitepaper
        double distanceFromLookaheadPointToRobotHeadingLine =
            Math.abs(headingUnitVector.getX() * this.getLookaheadWaypoint().getX() + headingUnitVector.getY() * this.getLookaheadWaypoint().getY() - c) /
            Math.sqrt(Math.pow(headingUnitVector.getX(), 2) + Math.pow(headingUnitVector.getY(), 2));

        double signedDistanceFromLookaheadPointToRobotHeadingLine = distanceFromLookaheadPointToRobotHeadingLine;
        // make distance negative if headingUnitVector is more counterclockwise than lookaheadVector
        if ((headingUnitVector.crossMagnitudeSigned(lookaheadVector) < 0) ^ !this.inverted) {
            signedDistanceFromLookaheadPointToRobotHeadingLine *= -1;
        }

        return signedDistanceFromLookaheadPointToRobotHeadingLine;
    }

    /**
     * calcualtes signed distance from lookahead point to robot line with variables
     * stored in pathfollower
     */
    public double calculateSignedDistanceFromLookaheadPointToRobotHeadingLine() {
        return calculateSignedDistanceFromLookaheadPointToRobotHeadingLine(this.getPath(), this.heading, this.xPosition, this.yPosition);
    }

    /**
     * calculates the target velocities for the left and right side drive
     * @param path
     * @param heading
     * @param xPosition
     * @param yPosition
     * @return an array of length two, where the first element is the left target velocity
     * and the second element is the right target velocity
     */
    public double[] calculateLeftAndRightTargetVelocities(ArrayList<Waypoint> path, double heading, double xPosition, double yPosition, double targetSpeed, double trackWidth, boolean inverted) {
        double signedDistanceFromLookaheadPointToRobotHeadingLine = this.calculateSignedDistanceFromLookaheadPointToRobotHeadingLine();
        double targetCurvature = 2 * signedDistanceFromLookaheadPointToRobotHeadingLine / Math.pow(Math.hypot(this.getLookaheadWaypoint().getX() - xPosition, this.getLookaheadWaypoint().getY()), 2);
        double targetVelocity = targetSpeed;

        if (inverted) {
            targetVelocity *= -1;
        }

        double leftTargetVelocity = targetVelocity * (2 - targetCurvature * trackWidth) / 2;
        double rightTargetVelocity = targetVelocity * (2 + targetCurvature * trackWidth) / 2;

        double[] leftAndRightTargetVelocities = {leftTargetVelocity, rightTargetVelocity};

        return leftAndRightTargetVelocities;
    }

    /**
     * calculates left and right target velocities with variables stored in PathFollower
     */
    public double[] calculateLeftAndRightTargetVelocities() {
        return calculateLeftAndRightTargetVelocities(this.path, this.heading, this.xPosition, this.yPosition, this.targetSpeed, this.trackWidth, this.inverted);
    }

    public double calculateFeedforward(double kV, double kA, double targetSpeed, double targetAcceleration) {
        return kV * targetSpeed + kA * targetAcceleration; 
    }

    /**
     * calculate feedforward with variables stored in pathfollower
     * @return
     */
    public double calculateFeedforward() {
        return calculateFeedforward(this.kV, this.kA, this.targetSpeed, this.targetAcceleration);
    }

    public boolean isPathDone() {
        if (this.getLastClosestPointIndex() >= (getPath().size() - 5)) {
            return true;
        } else {
            return false;
        }
    }

    public int getPreviousLookaheadPointIndex() {
        return this.previousLookaheadPointIndex;
    }

    private void setPreviousLookaheadPointIndex(int previousLookaheadPointIndex) {
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

    private void setLastClosestPointIndex(int lastClosestPointIndex) {
        this.lastClosestPointIndex = lastClosestPointIndex;
    }

    public void setPosition(double xPosition, double yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public void setHeading(double heading) {
        if (!inverted) {
            this.heading = heading;
        } else {
            this.heading = (heading + 180) % 360;
        }
    }

    public Waypoint getLookaheadWaypoint() {
        return this.lookaheadWaypoint;
    }
    
    private void setLookaheadWaypoint(Waypoint lookaheadWaypoint) {
        this.lookaheadWaypoint = lookaheadWaypoint;
    }
    
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    private void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public double getTargetSpeed() {
        return this.targetSpeed;
    }

    public double getLeftTargetVelocity() {
        return this.leftTargetVelocity;
    }

    private void setLeftTargetVelocity(double leftTargetVelocity) {
        this.leftTargetVelocity = leftTargetVelocity;
    }

    public double getRightTargetVelocity() {
        return this.rightTargetVelocity;
    }

    private void setRightTargetVelocity(double rightTargetVelocity) {
        this.rightTargetVelocity = rightTargetVelocity;
    }

    public double getFeedforward() {
        return this.feedforward;
    }

    private void setFeedforward(double feedforward) {
        this.feedforward = feedforward;
    }

    public void setTime(double time) {
        this.deltaTime = time - previousTime;
        this.previousTime = time;
    }
}
