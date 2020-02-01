package com.team766.frc2019.commands;

import java.util.ArrayList;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.paths.PathBuilder;
import com.team766.frc2019.paths.PathFollower;
import com.team766.frc2019.paths.Waypoint;
// import com.team766.frc2019.mechanisms.LimeLightI;
//import com.team766.hal.RobotProvider;
// import com.team766.hal.RobotProvider;

public class TurnAround extends Subroutine {

    public TurnAround() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("TurnAround STARTING");

        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
        waypoints.add(new Waypoint(0, 90));
        waypoints.add(new Waypoint(45, 0));
        waypoints.add(new Waypoint(30, -50));

        // System.out.println("Waypoints" + waypoints);

        // System.out.println(PathBuilder.GeneratePath(waypoints));
        ArrayList<Waypoint> path = new ArrayList<Waypoint>();
        path = PathBuilder.buildPath(waypoints);
        // PathFollower pathFollower = new PathFollower(path);
        // pathFollower.calculatemotorspeeds(path);

        System.out.println("path built");

        for (int i = 0; i < path.size(); i++) {
            System.out.println("Point " + i + ": " + path.get(i).getX() + " " + path.get(i).getY());
            Waypoint position = new Waypoint(0, 0);
            System.out.println("distance to 0,0 " + Waypoint.calculateDistanceBetweenTwoWaypoints(path.get(i), position));
            // System.out.println("("  + path.get(i).getX() + ", " + path.get(i).getY() + ")");
            // System.out.println("curvature: " + path.get(i).getCurvature());
        }

        PathFollower pathFollower = new PathFollower(path);

        // int closesetPointIndex = pathFollower.findClosestPointIndex(path, 0, Robot.drive.getXPosition(), Robot.drive.getYPosition());

        int closestPointIndex = pathFollower.findClosestPointIndex(path, 0, 0, 0);

        System.out.println("closest point index  " + closestPointIndex);
        System.out.println("x, y " + path.get(closestPointIndex).getX() + ", " + path.get(closestPointIndex).getY());
        // System.out.println("Max speeds:");
        // for (int i = 0; i < path.size(); i++) {
        //     // System.out.println("Point " + i + ": " + path.get(i).getX() + " " + path.get(i).getY());
        //     // System.out.println("("  + path.get(i).getX() + ", " + path.get(i).getY() + ")");
        //     System.out.println("max speed: " + path.get(i).getVelocity());
        // }

       // callSubroutine(new PreciseTurn((Robot.drive.getGyroAngle() + 180) % 360));
        
    }
}