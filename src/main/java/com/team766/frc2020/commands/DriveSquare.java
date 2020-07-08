package com.team766.frc2020.commands;

import java.util.ArrayList;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.paths.PathBuilder;
import com.team766.frc2020.paths.PathFollower;
import com.team766.frc2020.paths.Waypoint;
import com.team766.hal.RobotProvider;

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

        Robot.pathWebSocketServer.broadcastPath(path);

        // create a PathFollower that has functions that output steering error and target velocities
        PathFollower pathFollower = new PathFollower(path);
        pathFollower.setInverted(false);

        while(!pathFollower.isPathDone()) {

            // tell pathFollower our current position and heading and tell pathfollower to recalculate outputs
            pathFollower.setPosition(Robot.drive.getXPosition(), Robot.drive.getYPosition());
            pathFollower.setHeading(Robot.drive.getGyroAngle());
            pathFollower.setTime(RobotProvider.getTimeProvider().get());
            pathFollower.update();

            Robot.drive.setDriveVelocity(pathFollower.getLeftTargetVelocity(), pathFollower.getRightTargetVelocity(), pathFollower.getFeedforward());

            Robot.pathWebSocketServer.broadcastClosestPoint(
                path.get(pathFollower.getLastClosestPointIndex()).getX(),
                path.get(pathFollower.getLastClosestPointIndex()).getY()
            );
            Robot.pathWebSocketServer.broadcastLookaheadPoint(
                pathFollower.getLookaheadWaypoint().getX(),
                pathFollower.getLookaheadWaypoint().getY()
            );

            // allow odometry and other stuff to happen
            yield();
        }

        Robot.drive.setDrive(0, 0);
        System.out.println("path followed");
    }
}
