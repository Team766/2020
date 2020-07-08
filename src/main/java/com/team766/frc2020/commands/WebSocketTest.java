package com.team766.frc2020.commands;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.paths.PathBuilder;
import com.team766.frc2020.paths.PathFollower;
import com.team766.frc2020.paths.Waypoint;
import com.team766.hal.RobotProvider;
import com.team766.controllers.PIDController;
import com.team766.frc2020.mechanisms.Drive;
// import com.team766.frc2020.mechanisms.LimeLightI;
// import com.team766.hal.RobotProvider;

public class WebSocketTest extends Subroutine {

    protected void subroutine() {
        System.out.println("websockettest STARTING");
        double endOrientation;
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();

        boolean inverted = false;
        waypoints.add(new Waypoint(0, 0));
        waypoints.add(new Waypoint(0, 100));
        waypoints.add(new Waypoint(100, 100));
        waypoints.add(new Waypoint(100, 0));
        waypoints.add(new Waypoint(0, 0));
        endOrientation = Robot.drive.getGyroAngle();

        ArrayList<Waypoint> path = new ArrayList<Waypoint>();
        path = PathBuilder.buildPath(waypoints);
        for (int i = 0; i < path.size(); i++) {
            System.out.println("(" + path.get(i).getX() + "," + path.get(i).getY() + ")"); //built path coordinates
        }

        PathFollower pathFollower = new PathFollower(path);
        Robot.pathWebSocketServer.broadcastPath(path);

        SmartDashboard.putNumber("number of waypoints", path.size());
        PIDController m_turnController = new PIDController(0.01, 0.0001, 0.001, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        PIDController m_velocityController = new PIDController(0.01, 0.0001, 0.001, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        
        m_turnController.setSetpoint(0.0);
        int i = 0;
        while(!pathFollower.isPathDone()) {
            if (i % 15 == 0) {
                SmartDashboard.putNumber("last closest point index",  pathFollower.getLastClosestPointIndex());
                // TODO: refactor these into own functions
                Robot.pathWebSocketServer.broadcast("{\"closestPoint\": { \"x\": " + path.get(pathFollower.getLastClosestPointIndex()).getX() + ", \"y\": " + path.get(pathFollower.getLastClosestPointIndex()).getY() + "}}" );
                Robot.pathWebSocketServer.broadcast("{\"lookaheadPoint\": { \"x\": " + pathFollower.getLookaheadWaypoint().getX() + ", \"y\": " + pathFollower.getLookaheadWaypoint().getY() + "}}" );
                // System.out.println("steering error " + pathFollower.calculateSteeringError());
            }
            i++;

            pathFollower.setPosition(Robot.drive.getXPosition(), Robot.drive.getYPosition());
            if (!inverted) { 
                pathFollower.setHeading(Robot.drive.getGyroAngle());
            } else {
                pathFollower.setHeading((Robot.drive.getGyroAngle() + 180) % 360);
            }

            pathFollower.update();
            // m_turnController.calculate(pathFollower.calculateSteeringError(), true);
            double turnPower = m_turnController.getOutput() * 800;

            // System.out.println("closest point index" + pathFollower.findClosestPointIndex());
            // m_velocityController.setSetpoint(path.get(pathFollower.findClosestPointIndex()).getVelocity());
            // m_velocityController.calculate(Robot.drive.getVelocity() - path.get(pathFollower.findClosestPointIndex()).getVelocity(), true);
            // double straightPower = m_velocityController.getOutput();
            double straightPower = path.get(pathFollower.findClosestPointIndex()).getVelocity();

            if (!inverted) { 
                Robot.drive.setDrive((straightPower + turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600), (straightPower - turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600));
            } else {
                Robot.drive.setDrive( -1 * (straightPower - turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600), -1 * (straightPower + turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600));
            }

            // allow odometry and other stuff to happen
            yield();
        }
        // Robot.drive.setDrive(0, 0);
        System.out.println("path followed");
        // callSubroutine(new PreciseTurn(endOrientation));
        // System.out.println("final orientated");

    }
}