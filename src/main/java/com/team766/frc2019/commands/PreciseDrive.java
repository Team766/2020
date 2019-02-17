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
    double MIN_POWER = 0.2;
    double POWER_RAMP = 1.0;
    int driveDir = 1;

    public PreciseDrive(double targetAngle, double driveDistance, double targetPower, double endPower) {
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
        m_driveDistance = driveDistance;
        if (m_driveDistance < 0) {
            driveDir = -1;
        }
        m_targetAngle = targetAngle;
        m_targetPower = targetPower;
        m_endPower = endPower;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double index = 0;
        m_turnController.setSetpoint(0.0);
        System.out.println("TA: " + m_targetAngle + " Cu: " + Robot.drive.getGyroAngle() + " Diff: " + Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle) + " Pout: " + m_turnController.getOutput());
        while(getCurrentDistance() * driveDir < Math.abs(m_driveDistance)) {
            m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle), true);
            double turnPower = m_turnController.getOutput() * -Robot.drive.m_gyroDirection;
            double straightPower = calcPower() * driveDir;
            if (turnPower > 0) {
                Robot.drive.setDrive(straightPower - turnPower, straightPower, ControlMode.PercentOutput);
            } else {
                Robot.drive.setDrive(straightPower, straightPower + turnPower, ControlMode.PercentOutput);
            }
            if (index % 100 == 0) {
                System.out.println("TA: " + m_targetAngle + " Cu: " + Robot.drive.getGyroAngle() + " Diff: " + Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle) + " Pout: " + m_turnController.getOutput() + " Dist: " + getCurrentDistance() + " Gyro Dir: " + Robot.drive.m_gyroDirection + " DistPulse: " + Robot.drive.DIST_PER_PULSE);
            }
            index++;
        }
        Robot.drive.setDrive(m_endPower, m_endPower, ControlMode.PercentOutput);
        Robot.drive.resetEncoders();
        yield();
    }

    public double getCurrentDistance() {
        return(((Robot.drive.rightEncoderDistance() + Robot.drive.leftEncoderDistance())*Robot.drive.DIST_PER_PULSE)/2.0);
    }

    public double calcPower() {
        double currentDistance = getCurrentDistance();

        double endPower = ((Math.abs(m_driveDistance) - Math.abs(currentDistance))) * POWER_RAMP;
        return Math.max(Math.min(endPower, m_targetPower), MIN_POWER);
    }

}