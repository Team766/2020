package com.team766.frc2020.commands;

import java.util.ArrayList;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.paths.PathBuilder;
import com.team766.frc2020.paths.PathFollower;
import com.team766.frc2020.paths.Waypoint;

public class DriveSquare extends Subroutine {

    protected void subroutine() {

        System.out.println("DriveSquare STARTING");
        
        // create list of points for the robot to go to
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
        waypoints.add(new Waypoint(0, 0));
        waypoints.add(new Waypoint(0, 150));
        waypoints.add(new Waypoint(150, 150));
        waypoints.add(new Waypoint(150, 0));
        waypoints.add(new Waypoint(0, 0));

        // use PathBuilder to add points and data to help follow the waypoints
        ArrayList<Waypoint> path = new ArrayList<Waypoint>();
        path = PathBuilder.buildPath(waypoints);

        // create a PathFollower that has functions that output steering error and target velocities
        PathFollower pathFollower = new PathFollower(path);
        pathFollower.setInverted(false);

        while(!pathFollower.isPathDone()) {

            // tell pathFollower our current position and heading and tell it to update and recalculate
            pathFollower.setPosition(Robot.drive.getXPosition(), Robot.drive.getYPosition());
            pathFollower.setHeading(Robot.drive.getGyroAngle());
            pathFollower.update();

            double turnPower = pathFollower.getSteeringError();
            double straightPower = pathFollower.getTargetVelocity();

            Robot.drive.setDriveVelocity(straightPower + turnPower, straightPower - turnPower);

            // allow odometry and other stuff to happen
            yield();
        }

        Robot.drive.setDrive(0, 0);
        System.out.println("path followed");
    }
}
