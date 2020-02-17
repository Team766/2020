package com.team766.frc2019.commands;

import java.util.ArrayList;
import java.net.InetSocketAddress;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.java_websocket.server.WebSocketServer;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.paths.PathBuilder;
import com.team766.frc2019.paths.PathFollower;
import com.team766.frc2019.paths.Waypoint;
import com.team766.frc2019.paths.PathWebSocketServer;
import com.team766.hal.RobotProvider;
import com.team766.controllers.PIDController;
import com.team766.frc2019.mechanisms.Drive;
import com.team766.frc2019.commands.PreciseTurn;

// import com.team766.frc2019.mechanisms.LimeLightI;
// import com.team766.hal.RobotProvider;

public class TurnAround extends Subroutine {

    protected void subroutine() {
        System.out.println("TurnAround STARTING");
	        
        boolean inverted = false;
        double endOrientation;
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();

        waypoints.add(new Waypoint(0, 0));
        waypoints.add(new Waypoint(0, 50));
        waypoints.add(new Waypoint(50, 50));
        waypoints.add(new Waypoint(50, 0));
        waypoints.add(new Waypoint(0, 0));
        endOrientation = 0;

        // SQUARE WAYPOINTS
        // waypoints.add(new Waypoint(0, 0, 50, 50, 50));
        // waypoints.add(new Waypoint(0, 100, 50, 100, 50));
        // waypoints.add(new Waypoint(100, 100, 50, 100, 50));
        // waypoints.add(new Waypoint(100, 0, 50, 100, 50));
        // waypoints.add(new Waypoint(0, 0, 50, 0, 50));

        ArrayList<Waypoint> path = new ArrayList<Waypoint>();

        path = PathBuilder.buildPath(waypoints);

        for (int i = 0; i < path.size(); i++) {
            System.out.println("(" + path.get(i).getX() + "," + path.get(i).getY() + ")");
        }

        // make sure to pick waypoints or path correctly if testing
        PathFollower pathFollower = new PathFollower(path);

        System.out.println("path built");

        System.out.println(path.size() + " waypoints");

        SmartDashboard.putNumber("number of waypoints", path.size());

        PIDController m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());

        m_turnController.setSetpoint(0.0);
        int i = 0;
        while(!pathFollower.isPathDone()) {
            if (i % 100 == 0) {
                // System.out.println("position: " + Robot.drive.getXPosition() + ", " + Robot.drive.getYPosition());
                // System.out.printf("heading %.2f steering error angle %.2f pid output %.2f \n", Robot.drive.getGyroAngle(), pathFollower.calculateSteeringError(), m_turnController.getOutput());
                // System.out.println("last closest point index" + pathFollower.getLastClosestPointIndex());
                SmartDashboard.putNumber("last closest point index",  pathFollower.getLastClosestPointIndex());
                // System.out.println("lookahead point: " + pathFollower.getLookaheadWaypoint().getX() + ", " + pathFollower.getLookaheadWaypoint().getY());
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
        // PreciseTurn(endOrientation);  WHAT IS GOING ON IN THAT????????
        System.out.println("final orientated");
    }
}