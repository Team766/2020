package com.team766.frc2020.commands;

import java.util.ArrayList;

import com.team766.controllers.PIDController;
import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.mechanisms.LimeLight;
import com.team766.frc2020.mechanisms.LimeLight.CameraMode;
import com.team766.frc2020.mechanisms.LimeLight.LightMode;
import com.team766.frc2020.paths.PathBuilder;
import com.team766.frc2020.paths.PathFollower;
import com.team766.frc2020.paths.Waypoint;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MontePathsTeleopAuton extends Subroutine {

    private JoystickReader m_joystick1;
	private JoystickReader m_joystick2;
    private JoystickReader m_boxop;
	
	private double fwd_power = 0;
	private double turn_power = 0;
	private double leftPower = 0;
	private double rightPower = 0;

    public MontePathsTeleopAuton() {
        m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);
    }

    protected void subroutine() {
        
        while (Robot.drive.isEnabled()) {
            if (Robot.drive.isAutonomous() == false) { // teleop portion
                // System.out.println("joystick1: " + m_joystick1.getRawAxis(0) + "joystick2: " + m_joystick2.getRawAxis(1));
                // System.out.println("iteration");
                // System.out.println("joystick1: " + m_joystick1 + "joystick2: " + m_joystick2);

                if (Math.abs(m_joystick1.getRawAxis(1)) < 0.13 ) {
                    fwd_power = 0;
                } else {
                    fwd_power = -(0.05*(Math.abs(m_joystick1.getRawAxis(1))/m_joystick1.getRawAxis(1)) + Math.pow(m_joystick1.getRawAxis(1), 3));
                }
                if (Math.abs(m_joystick2.getRawAxis(0)) < 0.13 ) {
                    turn_power = 0;
                } else {
                    turn_power = (Math.abs(m_joystick2.getRawAxis(0))*1.3/m_joystick2.getRawAxis(0)) + Math.pow(m_joystick2.getRawAxis(0), 3);
                    turn_power = 0.20 * turn_power;
                    if (Math.abs(fwd_power) > 0.5) {
                        turn_power = 0.6 * turn_power;
                    }
                }

                double normalizer = Math.max(Math.abs(fwd_power),Math.abs(turn_power))/(Math.abs(fwd_power) + Math.abs(turn_power)); // divides both motor powers by the larger one to keep the ratio and keep power at or below 1
                double leftPower = (fwd_power + turn_power);
                double rightPower = (fwd_power - turn_power);
                
                //SmartDashboard.putNumber("Forward Power", fwd_power);
                //SmartDashboard.putNumber("Turn Power", turn_power);
                //SmartDashboard.putNumber("Left Power", leftPower);
                //SmartDashboard.putNumber("Right Power", rightPower);
                
                Robot.drive.setDrive(leftPower, rightPower);
                
                // if (m_boxop.getRawButton(5)) {
                //     Robot.climber.setClimberUpState(false);
                //     Robot.climber.setClimberDownState(true);
                // } else if (m_boxop.getRawButton(6)) {
                //     Robot.climber.setClimberUpState(true);
                //     Robot.climber.setClimberDownState(false);
                // }

                // if (m_boxop.getRawButton(7)) {
                // 	Robot.climber.setShifterPower(0);
                // } else if (m_boxop.getRawButton(8)) {
                // 	Robot.climber.setShifterPower(0.5);
                // }

                // if (m_boxop.getRawButton(9)) {
                // 	Robot.climber.setWinchPower(0);
                // } else if (m_boxop.getRawButton(10)) {
                // 	Robot.climber.setWinchPower(0.5);
                // }

                // if (m_boxop.getRawButton(3)) {
                // 	Robot.intake.setIntakePower(0);
                // } else if (m_boxop.getRawButton(4)) {
                // 	Robot.intake.setIntakePower(0.5);
                // }
                
                // if (m_boxop.getRawButton(1)) {
                // 	Robot.intake.setIntakeState(false);
                // } else if (m_boxop.getRawButton(2)) {
                // 	Robot.intake.setIntakeState(true);
                // }

                // if (m_joystick1.getRawButton(1)) {
                // 	Robot.outtake.setOuttakePower(0.5);
                // } else {
                // 	Robot.outtake.setOuttakePower(0);
                // }

                // if (m_boxop.getRawButton(18)) {
                // 	Robot.spinner.setSpinnerPower(0);
                // } else if (m_boxop.getRawButton(19)) {
                // 	Robot.spinner.setSpinnerPower(0.5);
                // }

                // if (m_boxop.getRawButton(20)) {
                // 	Robot.spinner.setSpinnerState(false);
                // } else if (m_boxop.getRawButton(21)) {
                // 	Robot.spinner.setSpinnerState(true);
                // }

                // if (m_boxop.getRawButton(22)) {
                // 	Robot.wagon.setWagonPower(0.5);
                // } else if (m_boxop.getRawButton(23)) {
                // 	Robot.wagon.setWagonPower(0);
                // }

                // if (m_joystick2.getRawButton(1)) {
                // 	Robot.waterwheel.setPusherState(true);
                // } else {
                // 	Robot.waterwheel.setPusherState(false);
                // }

                // if (m_joystick1.getRawButton(2)) {
                // 	Robot.waterwheel.setWheelPower(-0.1);
                // } else if (m_joystick2.getRawButton(2)) {
                // 	Robot.waterwheel.setWheelPower(0.1);
                // } else {
                // 	Robot.waterwheel.setWheelPower(0);
                // }
                
                if (!Robot.drive.isEnabled()) {
                    Robot.drive.nukeRobot();
                    return;
                }
            } else { // auton portion
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
                        Robot.pathWebSocketServer.broadcast("{\"closest point\": { \"x\": " + path.get(pathFollower.getLastClosestPointIndex()).getX() + ", \"y\": " + path.get(pathFollower.getLastClosestPointIndex()).getY() + "}}" );
                        Robot.pathWebSocketServer.broadcast("{\"lookahead point\": { \"x\": " + pathFollower.getLookaheadWaypoint().getX() + ", \"y\": " + pathFollower.getLookaheadWaypoint().getY() + "}}" );
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
                    m_turnController.calculate(pathFollower.calculateSteeringError(), true);
                    double turnPower = m_turnController.getOutput() * 800;

                    // System.out.println("closest point index" + pathFollower.findClosestPointIndex());
                    m_velocityController.setSetpoint(path.get(pathFollower.findClosestPointIndex()).getVelocity());
                    m_velocityController.calculate(Robot.drive.getVelocity() - path.get(pathFollower.findClosestPointIndex()).getVelocity(), true);
                    double straightPower = m_velocityController.getOutput();

                    // if (!inverted) { 
                    //     Robot.drive.setDrive((straightPower + turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600), (straightPower - turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600));
                    // } else {
                    //     Robot.drive.setDrive( -1 * (straightPower - turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600), -1 * (straightPower + turnPower) / ( 15 * 12 * 60 / 6.25 * 256 / 600));
                    // }

                    // allow odometry and other stuff to happen
                    yield();
                }
                // Robot.drive.setDrive(0, 0);
                System.out.println("path followed");
                // callSubroutine(new PreciseTurn(endOrientation));
                System.out.println("final orientated");
            }
        }
    }
}