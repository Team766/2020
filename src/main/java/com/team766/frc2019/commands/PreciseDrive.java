package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
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

    public PreciseDrive(double driveDistance, double targetAngle, double targetPower, double startPower, double endPower) {
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
        m_driveDistance = driveDistance;
        m_targetAngle = targetAngle;
        m_targetPower = targetPower;
        m_startPower = startPower;
        m_endPower = endPower;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        m_turnController.setSetpoint(0.0);
        while(getCurrentDistance() < m_driveDistance) {
            m_turnController.calculate(Robot.drive.AngleDifference(m_targetAngle, Robot.drive.getGyroAngle()), true);
            double turnPower = m_turnController.getOutput();
            double straightPower = calcPower();
            if (turnPower < 0) {
                Robot.drive.setDrivePower(straightPower + turnPower, straightPower);
            } else {
                Robot.drive.setDrivePower(straightPower, straightPower - turnPower);
            }
            System.out.println("Turn: " + turnPower + " Straight: " + straightPower + " Current: " + Robot.drive.getGyroAngle() + " Diff: " + Robot.drive.AngleDifference(m_targetAngle, Robot.drive.getGyroAngle()));
            yield();
        }
        Robot.drive.setDrivePower(m_endPower, m_endPower);
        Robot.drive.resetEncoders();
    }

    public double getCurrentDistance() {
        return(((Robot.drive.rightEncoderDistance() + Robot.drive.leftEncoderDistance())*Robot.drive.DIST_PER_PULSE)/2.0);
    }

    public double calcPower() {
        double currentDistance = getCurrentDistance();  
        
        double startPower = currentDistance * POWER_RAMP;
        double endPower = (m_driveDistance - currentDistance) * POWER_RAMP;
        return Math.min(Math.min(startPower, endPower), m_targetPower) + MIN_POWER;

    }

}