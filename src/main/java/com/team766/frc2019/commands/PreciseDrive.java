package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import javax.swing.text.StyleContext.SmallAttributeSet;

import com.team766.controllers.PIDController;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;

public class PreciseDrive extends Subroutine {

    private JoystickReader m_joystick1  = RobotProvider.instance.getJoystick(1);
    PIDController m_turnController;
    double m_driveDistance;
    double m_targetAngle;
    double m_targetPower;
    double m_startPower;
    double m_endPower;
    double m_adjustment;
    double MIN_POWER = 0.25;
    double POWER_RAMP = 1.0;
    int driveDir = 1;
    double END_POWER_PERCENT = 0.75;

    /**
     * Precisely drives for the set parameters.
     * @param targetAngle
     * @param driveDistance
     * @param targetPower
     * @param endPower
     */
    public PreciseDrive(double targetAngle, double driveDistance, double targetPower, double endPower) {
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        m_driveDistance = driveDistance;
        if (m_driveDistance < 0) {
            driveDir = -1;
        }
        m_targetAngle = targetAngle;
        m_targetPower = targetPower;
        m_endPower = endPower;
        //takeControl(Robot.drive);
    }

    protected void subroutine() {
        Robot.drive.resetEncoders();
        System.out.println("Reset encoders, current distance: " + getCurrentDistance());
        double index = 0;
        m_turnController.setSetpoint(0.0);
        System.out.println("I'm driving to: " + m_targetAngle + " and the difference is : " + Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle) + " with an power of: " + m_targetPower + " to a distance of: " + m_driveDistance);
        while(getCurrentDistance() * driveDir < Math.abs(m_driveDistance) && (Math.abs(m_joystick1.getRawAxis(1)) < .2)) {
            m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle), true);
            double turnPower = m_turnController.getOutput();
            double straightPower = calcPower(Math.abs(getCurrentDistance() / m_driveDistance)) * driveDir;
            if (driveDir < 0) {
                if (turnPower > 0) {
                    Robot.drive.setDrive(straightPower, straightPower + turnPower);
                } else {
                    Robot.drive.setDrive(straightPower - turnPower, straightPower);
                }
            } else {
                if (turnPower > 0) {
                    Robot.drive.setDrive(straightPower - turnPower, straightPower);
                } else {
                    Robot.drive.setDrive(straightPower, straightPower + turnPower);
                }
            }
            /*
            SmartDashboard.putNumber("Target Angle", m_targetAngle);
            SmartDashboard.putNumber("Current Angle", Robot.drive.getGyroAngle());
            SmartDashboard.putNumber("Current Error", m_turnController.getCurrentError());
            SmartDashboard.putNumber("PID Output", m_turnController.getOutput());
            SmartDashboard.putNumber("Current Distance", getCurrentDistance());
            SmartDashboard.putNumber("Target Power", calcPower(Math.abs(getCurrentDistance() / m_driveDistance)));
            SmartDashboard.putNumber("Left Power", Robot.drive.leftMotorVelocity());
            SmartDashboard.putNumber("Right Power", Robot.drive.rightMotorVelocity());
            SmartDashboard.putNumber("Drive Direction", driveDir);
            */
            if (!Robot.drive.isEnabled()){
                Robot.drive.nukeRobot();
                m_turnController.reset();
                return;
            }
        }
        if (!(Math.abs(m_joystick1.getRawAxis(1)) < .2)) {
            callSubroutine(new TeleopAuton());
        }
        System.out.println("PreciseDrive finished");
        Robot.drive.setDrive(m_endPower, m_endPower);
        Robot.drive.resetEncoders();
        yield();
        return;
    }

    public double getCurrentDistance() {
        return(((Robot.drive.rightEncoderDistance() + Robot.drive.leftEncoderDistance())*Robot.drive.DIST_PER_PULSE)/2.0);
    }

    private double calcPower(double arcPercent) {
        //double endPower = (((m_endPower - m_targetPower) / (1 - arcPercent)) * (arcPercent - END_POWER_PERCENT)) + m_targetPower;
        //return Math.max(Math.min(Math.abs(endPower), Math.abs(m_targetPower)), MIN_POWER) * moveDir;

        if (arcPercent < END_POWER_PERCENT) {
            return m_targetPower;
        }
        double scaledPower = (1 - (arcPercent - END_POWER_PERCENT) / (1 - END_POWER_PERCENT)) * m_targetPower;
        if (m_targetPower >= 0) {
            return Math.max(MIN_POWER, scaledPower);
        } else {
            return Math.min(-MIN_POWER, scaledPower);
        }
        //return m_targetPower;
    }

}