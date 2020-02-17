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

// import com.team766.frc2019.mechanisms.LimeLightI;
// import com.team766.hal.RobotProvider;

public class TurnAround extends Subroutine {

    protected void subroutine() {
        System.out.println("TurnAround STARTING");
	        
        boolean inverted = true;
        double endOrientation;
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();

        waypoints.add(new Waypoint(0, 0));
        waypoints.add(new Waypoint(0, -25));
        waypoints.add(new Waypoint(-25, -25));
        waypoints.add(new Waypoint(-25, 0));
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

            System.out.println("steering error " + pathFollower.calculateSteeringError());

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
        callSubroutine(new PreciseTurn(endOrientation));
        // PreciseTurn(endOrientation); 
        System.out.println("final orientated");
    }


// why did i put this here? bruh
	// private void PreciseTurn(double endOrientation) {
    //     PIDController m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
    //     boolean turning = true;
    //     double power = 0;
    //     double m_turnAngle = endOrientation;
    //     System.out.println("hey im gonna turn");
    //     m_turnController.setSetpoint(0.0);
    //     m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_turnAngle), true);
    //     while((!(Robot.drive.isTurnDone(m_turnController)))) {
    //         m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_turnAngle), true);
    //         power = m_turnController.getOutput();

    //         Robot.drive.setDrive(-power, power);

    //         SmartDashboard.putNumber("Current Angle", Robot.drive.getGyroAngle());
    //         SmartDashboard.putNumber("Target Angle", m_turnAngle);
    //         SmartDashboard.putNumber("Angle Difference", Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_turnAngle));
    //         SmartDashboard.putNumber("PID Output", m_turnController.getOutput());

    //         if (!Robot.drive.isEnabled()){
    //             turning = false;
    //             Robot.drive.nukeRobot();
    //             yield();
    //             return;
    //         }
    //     }
    //     // if (!(Math.abs(m_joystick1.getRawAxis(1)) < .2)) {
    //     //     //callSubroutine(new TeleopAuton());
    //     // }
    //     Robot.drive.setDrive(0.0, 0.0);
    //     // Robot.drive.resetEncoders();
    //     yield();
    //     turning = false;
    //     System.out.println("exited loop");
	// }
}