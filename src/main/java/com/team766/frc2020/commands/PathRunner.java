package com.team766.frc2020.commands;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.net.InetSocketAddress;
import org.java_websocket.server.WebSocketServer;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.paths.PathBuilder;
import com.team766.frc2020.paths.PathFollower;
import com.team766.frc2020.paths.Waypoint;
import com.team766.hal.RobotProvider;
import com.team766.controllers.PIDController;
import com.team766.frc2020.mechanisms.Drive;
import com.team766.frc2020.paths.PathWebSocketServer;
// import com.team766.frc2020.mechanisms.LimeLightI;
// import com.team766.hal.RobotProvider;

public class PathRunner extends Subroutine {

    protected void subroutine() {
        System.out.println("PathRunner STARTING");
        PathWebSocketServer pathWebSocketServer = new PathWebSocketServer(new InetSocketAddress("10.7.66.2", 5801));
        pathWebSocketServer.start();
        boolean inverted = true;
        double endOrientation;
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();

        waypoints.add(new Waypoint(0, 0));
        waypoints.add(new Waypoint(0, -25));
        waypoints.add(new Waypoint(-25, -25));
        waypoints.add(new Waypoint(-25, 0));
        waypoints.add(new Waypoint(0, 0));
        endOrientation = Robot.drive.getGyroAngle();

        ArrayList<Waypoint> path = new ArrayList<Waypoint>();
        path = PathBuilder.buildPath(waypoints);
        for (int i = 0; i < path.size(); i++) {
            System.out.println("(" + path.get(i).getX() + "," + path.get(i).getY() + ")"); //built path coordinates
        }

        PathFollower pathFollower = new PathFollower(path);
        pathWebSocketServer.broadcastPath(path);

        SmartDashboard.putNumber("number of waypoints", path.size());
        PIDController m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        m_turnController.setSetpoint(0.0);
        int i = 0;
        while(!pathFollower.isPathDone()) {
            if (i % 15 == 0) {

                SmartDashboard.putNumber("last closest point index",  pathFollower.getLastClosestPointIndex());
                // TODO: refactor these into own functions
                pathWebSocketServer.broadcast("{\"position\": { \"x\": " + Robot.drive.getXPosition() + ", \"y\": " + Robot.drive.getYPosition() + "}}" );
                pathWebSocketServer.broadcast("{\"heading\": " + Robot.drive.getGyroAngle() + "}" );
                pathWebSocketServer.broadcast("{\"closest point\": { \"x\": " + path.get(pathFollower.getLastClosestPointIndex()).getX() + ", \"y\": " + path.get(pathFollower.getLastClosestPointIndex()).getY() + "}}" );
                pathWebSocketServer.broadcast("{\"lookahead point\": { \"x\": " + pathFollower.getLookaheadWaypoint().getX() + ", \"y\": " + pathFollower.getLookaheadWaypoint().getY() + "}}" );
                System.out.println("steering error " + pathFollower.calculateSteeringError());
            }
            i++;

            pathFollower.setPosition(Robot.drive.getXPosition(), Robot.drive.getYPosition());
            if (!inverted) { 
                pathFollower.setHeading(Robot.drive.getGyroAngle());
            } else {
                pathFollower.setHeading((Robot.drive.getGyroAngle() + 180) % 360);
            }

            pathFollower.update();
            m_turnController.calculate(pathFollower.calculateSteeringError(), true);
            double turnPower = m_turnController.getOutput() * 500;

            // double straightPower = path.get(previousLookaheadPointIndex).getVelocity();
            // System.out.println("closest point index" + findClosestPointIndex());
            double straightPower = path.get(pathFollower.findClosestPointIndex()).getVelocity();

            if (!inverted) { 
                Robot.drive.setDrive((straightPower + turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600), (straightPower - turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600));
            } else {
                Robot.drive.setDrive( -1 * (straightPower - turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600), -1 * (straightPower + turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600));
            }

            // allow odometry and other stuff to happen
            yield();
        }
        Robot.drive.setDrive(0, 0);
        System.out.println("path followed");
        //callSubroutine(new PreciseTurn(endOrientation));
        System.out.println("final orientated");
        callSubroutine(new PathRunner());

        // continues to print position
        while(true) {   
            if (i % 15 == 0) {
                pathWebSocketServer.broadcast("{\"position\": { \"x\": " + Robot.drive.getXPosition() + ", \"y\": " + Robot.drive.getYPosition() + "}}" );
                pathWebSocketServer.broadcast("{\"heading\": " + Robot.drive.getGyroAngle() + "}" );
            }
        }
    }
}