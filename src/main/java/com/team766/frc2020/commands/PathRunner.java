package com.team766.frc2020.commands;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.paths.PathBuilder;
import com.team766.frc2020.paths.PathFollower;
import com.team766.frc2020.paths.Waypoint;
import com.team766.hal.RobotProvider;

public class PathRunner extends Subroutine {

    protected void subroutine() {
        System.out.println("PathRunner STARTING");
        double endOrientation;
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
        
        boolean inverted = false;
        waypoints.add(new Waypoint(0, 0));
        waypoints.add(new Waypoint(0, 150));
        waypoints.add(new Waypoint(150, 150));
        waypoints.add(new Waypoint(150, 0));
        waypoints.add(new Waypoint(0, 0));
        endOrientation = (Robot.drive.getGyroAngle() + 180) % 360;

        ArrayList<Waypoint> path = new ArrayList<Waypoint>();
        path = PathBuilder.buildPath(waypoints);
        // for (int i = 0; i < path.size(); i++) {
        //     System.out.println("(" + path.get(i).getX() + "," + path.get(i).getY() + ")"); //built path coordinates
        // }

        PathFollower pathFollower = new PathFollower(path);
        pathFollower.setInverted(inverted);
        Robot.pathWebSocketServer.broadcastPath(path);

        int i = 0;
        while(!pathFollower.isPathDone()) {
            if (i % 15 == 0) {
                SmartDashboard.putNumber("last closest point index",  pathFollower.getLastClosestPointIndex());

                Robot.pathWebSocketServer.broadcastClosestPoint(
                    path.get(pathFollower.getLastClosestPointIndex()).getX(),
                    path.get(pathFollower.getLastClosestPointIndex()).getY()
                );
                Robot.pathWebSocketServer.broadcastLookaheadPoint(
                    pathFollower.getLookaheadWaypoint().getX(),
                    pathFollower.getLookaheadWaypoint().getY()
                );

                // System.out.println("steering error " + pathFollower.calculateSteeringError());
            }
            i++;

            pathFollower.setPosition(Robot.drive.getXPosition(), Robot.drive.getYPosition());
            pathFollower.setHeading(Robot.drive.getGyroAngle());

            pathFollower.update();

            System.out.println("closest point index" + pathFollower.findClosestPointIndex());

            pathFollower.setPosition(Robot.drive.getXPosition(), Robot.drive.getYPosition());
            pathFollower.setHeading(Robot.drive.getGyroAngle());
            pathFollower.setTime(RobotProvider.getTimeProvider().get());
            pathFollower.update();

            Robot.drive.setDriveVelocity(pathFollower.getLeftTargetVelocity(), pathFollower.getRightTargetVelocity(), pathFollower.getFeedforward());

            // allow odometry and other stuff to happen
            yield();
        }

        Robot.drive.setDrive(0, 0);
        System.out.println("path followed");
        callSubroutine(new PreciseTurn(endOrientation));
        System.out.println("final orientated");

        
        // //-------------------------------------------------------------------------------------------
        // //  UNCOMMENT FOR A SECOND PATH; ONLY FOR TEMPORARY TESTING, YOU CAN DELETEME
        // // should do same path but drive in reverse
        // PathFollower pathFollower2 = new PathFollower(path);
        // pathFollower.setInverted(true);
        // Robot.pathWebSocketServer.broadcastPath(path);

        // int j = 0;
        // while(!pathFollower2.isPathDone()) {
        //     if (j % 15 == 0) {
        //         SmartDashboard.putNumber("last closest point index",  pathFollower2.getLastClosestPointIndex());

        //         Robot.pathWebSocketServer.broadcastClosestPoint(
        //             path.get(pathFollower2.getLastClosestPointIndex()).getX(),
        //             path.get(pathFollower2.getLastClosestPointIndex()).getY()
        //         );
        //         Robot.pathWebSocketServer.broadcastLookaheadPoint(
        //             pathFollower2.getLookaheadWaypoint().getX(),
        //             pathFollower2.getLookaheadWaypoint().getY()
        //         );

        //         // System.out.println("steering error " + pathFollower2.calculateSteeringError());
        //     }
        //     j++;

        //     pathFollower2.setPosition(Robot.drive.getXPosition(), Robot.drive.getYPosition());
        //     pathFollower2.setHeading(Robot.drive.getGyroAngle());

        //     pathFollower2.update();

        //     System.out.println("closest point index" + pathFollower2.findClosestPointIndex());

        //     pathFollower2.setPosition(Robot.drive.getXPosition(), Robot.drive.getYPosition());
        //     pathFollower2.setHeading(Robot.drive.getGyroAngle());
        //     pathFollower2.setTime(RobotProvider.getTimeProvider().get());
        //     pathFollower2.update();

        //     Robot.drive.setDriveVelocity(pathFollower2.getLeftTargetVelocity(), pathFollower2.getRightTargetVelocity(), pathFollower2.getFeedforward());

        //     // allow odometry and other stuff to happen
        //     yield();
        // }
    }
}