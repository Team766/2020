package com.team766.frc2019.commands;

import java.util.ArrayList;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.paths.PathBuilder;
import com.team766.frc2019.paths.PathFollower;
import com.team766.frc2019.paths.Waypoint;
import com.team766.hal.RobotProvider;
import com.team766.controllers.PIDController;
import com.team766.frc2019.mechanisms.Drive;

// import com.team766.frc2019.mechanisms.LimeLightI;
// import com.team766.hal.RobotProvider;

public class TurnAround extends Subroutine {
    public TurnAround() {
        //takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("TurnAround STARTING");

        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();

        waypoints.add(new Waypoint(0, 0, 50, 50, 50));
        waypoints.add(new Waypoint(0, 100, 50, 100, 50));
        waypoints.add(new Waypoint(100, 100, 50, 100, 50));
        waypoints.add(new Waypoint(100, 0, 50, 100, 50));
        waypoints.add(new Waypoint(0, 0, 50, 0, 50));

        // SQUARE WAYPOINTS
        // waypoints.add(new Waypoint(0, 0, 50, 50, 50));
        // waypoints.add(new Waypoint(0, 100, 50, 100, 50));
        // waypoints.add(new Waypoint(100, 100, 50, 100, 50));
        // waypoints.add(new Waypoint(100, 0, 50, 100, 50));
        // waypoints.add(new Waypoint(0, 0, 50, 0, 50));

        ArrayList<Waypoint> path = new ArrayList<Waypoint>();

        path = PathBuilder.buildPath(waypoints);

        for (int i = 0; i < path.size(); i++) {
            // System.out.println("(" + path.get(i).getX() + "," + path.get(i).getY() + ")" + "velocity " + path.get(i).getVelocity());
            System.out.println("(" + path.get(i).getX() + "," + path.get(i).getY() + ")");
            //System.out.println("heading error: " + pathFollower.calculateSteeringError(path, 0.0, Robot.drive.getXPosition(), Robot.drive.getYPosition(), i));
        }

        // make sure to pick waypoints or path correctly if testing
        PathFollower pathFollower = new PathFollower(path);

        System.out.println("path built");

        System.out.println(path.size() + " waypoints");

        PIDController m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());

        boolean isPathDone = false;

        m_turnController.setSetpoint(0.0);
        int i = 0;
        while(!isPathDone) {
            if (i % 100 == 0) {
                // System.out.println("position: " + Robot.drive.getXPosition() + ", " + Robot.drive.getYPosition());
                System.out.printf("heading %.2f steering error angle %.2f pid output %.2f \n", Robot.drive.getGyroAngle(), pathFollower.calculateSteeringError(), m_turnController.getOutput());
                System.out.println("last closest point index" + pathFollower.getLastClosestPointIndex());
                // System.out.println("lookahead point: " + pathFollower.getLookaheadWaypoint().getX() + ", " + pathFollower.getLookaheadWaypoint().getY());
            }
            i++;

            pathFollower.setPosition(Robot.drive.getXPosition(), Robot.drive.getYPosition());
            
            if ((pathFollower.calculateSteeringError() > 90) || (pathFollower.calculateSteeringError()< -90)) {
                Drive.setInverted(true);
                Drive.invertMotors();
            }
            
            if (!Drive.getInverted()) { //TODO: Make sure this angle stuff behaves properly (domain of angles and stuff)
                pathFollower.setHeading(Robot.drive.getGyroAngle());
            } else {
                pathFollower.setHeading(Robot.drive.getGyroAngle() + 180);
            }
            pathFollower.update();

            if (pathFollower.isPathDone()) {
                Robot.drive.setDrive(0, 0);
                Drive.resetMotorOrientation();
                System.out.println("path followed");
                return;
            }

            m_turnController.calculate(pathFollower.calculateSteeringError(), true);
            double turnPower = m_turnController.getOutput() * 500;
            // double straightPower = path.get(previousLookaheadPointIndex).getVelocity();
            // System.out.println("closest point index" + findClosestPointIndex());
            double straightPower = path.get(pathFollower.findClosestPointIndex()).getVelocity();

            if (!Drive.getInverted()) { 
                Robot.drive.setDrive(straightPower + turnPower, straightPower - turnPower);
            } else {
                Robot.drive.setDrive(straightPower - turnPower, straightPower + turnPower);
            }
            
            // allow odometry and other stuff to happen
            yield();
        }
    }
}