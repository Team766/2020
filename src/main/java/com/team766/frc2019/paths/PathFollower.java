package com.team766.frc2019.paths;

import java.util.ArrayList;

import com.team766.frc2019.paths.Waypoint;

//  Find the closest point
// ● Find the lookahead point
// ● Calculate the curvature of the arc to the lookahead point
// ● Calculate the target left and right wheel velocities
// ● Use a control loop to achieve the target left and right wheel velocities
public class PathFollower {
    ArrayList<Waypoint> path = new ArrayList<Waypoint>();

    public PathFollower(ArrayList<Waypoint> path) {
        this.path = path;
    }

    public void findLookaheadPoint(ArrayList<Waypoint> path, double xPosition, double yPosition, int indexOfPreviousLookaheadPoint, double lookaheadDistance) {
        a = d.Dot(d);
        b = 2*f.Dot(d);
        c = f.Dot(f) - r*r;
        discriminant = b*b - 4*a*c;
        if (discriminant < 0) {
        // no intersection
        }else{
        discriminant = sqrt(discriminant)
        t1 = (-b - discriminant)/(2*a)
        t2 = (-b + discriminant)/(2*a)
        if (t1 >= 0 && t1 <=1){
        //return t1 intersection
        }
        if (t2 >= 0 && t2 <=1){
        //return t2 intersection
        }
        //otherwise, no intersection
        }
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
