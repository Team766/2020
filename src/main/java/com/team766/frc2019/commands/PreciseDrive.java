package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.controllers.PIDController;

public class PreciseDrive extends Subroutine {

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
    double END_POWER_PERCENT = 0.85;

    /**
     * Precisely drives for the set parameters.
     * @param targetAngle
     * @param driveDistance
     * @param targetPower
     * @param endPower
     */
    public PreciseDrive(double targetAngle, double driveDistance, double targetPower, double endPower) {
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
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
        double index = 0;
        m_turnController.setSetpoint(0.0);
        System.out.println("I'm driving to: " + m_targetAngle + " and the difference is : " + Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle) + " with an power of: " + m_targetPower + " to a distance of: " + m_driveDistance);
        while(getCurrentDistance() * driveDir < Math.abs(m_driveDistance)) {
            m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle), true);
            double turnPower = m_turnController.getOutput();
            double straightPower = calcPower(Math.abs(getCurrentDistance() / m_driveDistance)) * driveDir;
            if (driveDir < 0) {
                if (turnPower > 0) {
                    Robot.drive.setDrive(straightPower, straightPower + turnPower, ControlMode.PercentOutput);
                } else {
                    Robot.drive.setDrive(straightPower - turnPower, straightPower, ControlMode.PercentOutput);
                }
            } else {
                if (turnPower > 0) {
                    Robot.drive.setDrive(straightPower - turnPower, straightPower, ControlMode.PercentOutput);
                } else {
                    Robot.drive.setDrive(straightPower, straightPower + turnPower, ControlMode.PercentOutput);
                }
            }
            if (index % 15 == 0 && Robot.drive.isEnabled()) {
                System.out.println("TA: " + m_targetAngle + " Cu: " + Robot.drive.getGyroAngle() + " Diff: " + Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle) + " Pout: " + m_turnController.getOutput() + " Dist: " + getCurrentDistance() + " Power: " + calcPower(Math.abs(getCurrentDistance() / m_driveDistance))+ " Left Power: " + Robot.drive.leftMotorVelocity() + " Right Power: " + Robot.drive.rightMotorVelocity() + " DriveDir: " + driveDir);
            }
            index++;
            if (!Robot.drive.isEnabled()){
                Robot.drive.nukeRobot();
                m_turnController.reset();
                return;
            }
        }
        System.out.println("PreciseDrive finished");
        Robot.drive.setDrive(m_endPower, m_endPower, ControlMode.PercentOutput);
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