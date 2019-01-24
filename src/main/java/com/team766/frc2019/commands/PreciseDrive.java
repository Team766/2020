package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.controllers.PIDController;

public class PreciseDrive extends Subroutine {

    double m_driveTime;
    PIDController m_turnController;
    double m_leftPower;
    double m_rightPower;

    public PreciseDrive(double driveTime, double leftPower, double rightPower) {
        m_driveTime = driveTime;
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
        m_leftPower = leftPower;
        m_rightPower = rightPower;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double currentAngle = Robot.drive.getGyroAngle();
        m_turnController.setSetpoint(currentAngle);
        double power = 0;
        power = m_turnController.getOutput();
        double endTime = System.currentTimeMillis() + (m_driveTime * 1000);
        System.out.println("Current Angle: " + currentAngle + " Power: " + power + "Time: " + System.currentTimeMillis() + " End Time: " + endTime);
        m_turnController.calculate(Robot.drive.getGyroAngle(), true);
        while(System.currentTimeMillis() < endTime) {
            m_turnController.calculate(Robot.drive.getGyroAngle(), true);
            power = m_turnController.getOutput();
            if (power < 0) {
                Robot.drive.setDrivePower(m_leftPower + power, m_rightPower);
            } else {
                Robot.drive.setDrivePower(m_leftPower, m_rightPower - power);
            }
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}