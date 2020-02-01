package com.team766.frc2019.paths;

import java.util.ArrayList;

import com.team766.frc2019.paths.Waypoint;
import com.team766.frc2019.paths.Vector;

//  Find the closest point
//  Find the lookahead point
//  Calculate the curvature of the arc to the lookahead point
//  Calculate the target left and right wheel velocities
//  Use a control loop to achieve the target left and right wheel velocities
public class PathFollower {
    ArrayList<Waypoint> path = new ArrayList<Waypoint>();

    public PathFollower(ArrayList<Waypoint> path) {
        this.path = path;
    }
    
    //     1. E is the starting point of the line segment
    // 2. L is the end point of the line segment
    // 3. C is the center of circle (robot location)
    // 4. r is the radius of that circle (lookahead distance)
    // 10
    // Compute:
    // d = L - E (Direction vector of ray, from start to end)
    // f = E - C (Vector from center sphere to ray start)
    /**
     * @param lookaheadDistance radius of look ahead distance (12 - 15)
     */
    public Waypoint findLookaheadPoint(ArrayList<Waypoint> path, double xPosition, double yPosition, int previousLookaheadPointIndex, double lookaheadDistance) {
        for (int i = previousLookaheadPointIndex; i < path.size() - 1; i++) {
            Vector lineSegmentVector = new Vector(path.get(i + 1).getX() - path.get(i).getX(), path.get(i + 1).getY() - path.get(i).getY());
            Vector centerToRayStartVector = new Vector(
                path.get(i).getX() - xPosition,
                path.get(i).getY() - yPosition
            );
            // a b and c are from the quadratic formula
            double a = lineSegmentVector.dot(lineSegmentVector);
            // System.out.println("linSegmentVector x" + lineSegmentVector.getX());
            // System.out.println("line segment vector y" + lineSegmentVector.getY());
            // System.out.println("a " + a);
            double b = 2 * centerToRayStartVector.dot(lineSegmentVector);
            double c = centerToRayStartVector.dot(centerToRayStartVector) - Math.pow(lookaheadDistance, 2);
            double discriminant = Math.pow(b, 2) - 4 * a * c;
            // System.out.println("i is " + i);
            if (discriminant < 0) {
                // System.out.println("discriminant=" + discriminant + "<0");
            // no intersection
            } else {
                discriminant = Math.sqrt(discriminant);
                // System.out.println("discriminant=" + discriminant + ">0");

                // t1 and t2 are the two values from the quadratic formula
                double t1 = (-b - discriminant) / (2 * a);
                double t2 = (-b + discriminant) / (2 * a);

                // Point = E + (t value of intersection) * d
                // if intersection exists find values
                if (t1 >= 0 && t1 <=1) {
                    // System.out.println("intersection t1=" + t1 + " at i=" + i);
                    return path.get(i).add(new Waypoint(lineSegmentVector.getX() * t1, lineSegmentVector.getY() * t2));
                    //return t1 intersection
                }
                if (t2 >= 0 && t2 <=1) {
                    //return t2 intersection
                    // System.out.println("intersection t2=" + t2 +" at i=" + i);
                    return path.get(i).add(new Waypoint(lineSegmentVector.getX() * t2, lineSegmentVector.getY() * t2));
                }
                // System.out.println("no intersection");
            }
        }
        //otherwise, no intersection
        return path.get(previousLookaheadPointIndex);
    }

    public int findClosestPointIndex(ArrayList<Waypoint> path, int lastClosestPointIndex, double xPosition, double yPosition) {
        Waypoint position = new Waypoint(xPosition, yPosition);

        // set smallest distance to last known smallest point
        // and set smallest index to that point
        double smallestDistance = Waypoint.calculateDistanceBetweenTwoWaypoints(path.get(lastClosestPointIndex), position);
        int smallestIndex = lastClosestPointIndex;

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

	public static void followPath() {
	}

}
