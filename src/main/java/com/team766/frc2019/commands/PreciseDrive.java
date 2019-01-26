package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.controllers.PIDController;

public class PreciseDrive extends Subroutine {

    PIDController m_turnController;
    double m_driveTime;
    double m_targetAngle;
    double m_leftPower;
    double m_rightPower;

    public PreciseDrive(double driveTime, double targetAngle, double leftPower, double rightPower) {
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
        m_driveTime = driveTime;
        m_targetAngle = targetAngle;
        m_leftPower = leftPower;
        m_rightPower = rightPower;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        m_turnController.setSetpoint(m_targetAngle);
        double power = 0;
        power = m_turnController.getOutput();
        double endTime = System.currentTimeMillis() + (m_driveTime * 1000);
        System.out.println("Current Angle: " + Robot.drive.getGyroAngle() + " Target Angle: " + m_targetAngle + " Power: " + power + "Time: " + System.currentTimeMillis() + " End Time: " + endTime);
        m_turnController.calculate(Robot.drive.getGyroAngle(), true);
        while(System.currentTimeMillis() < endTime) {
            m_turnController.calculate(Robot.drive.getGyroAngle(), true);
            power = m_turnController.getOutput();
            if (power < 0) {
                Robot.drive.setDrivePower(m_leftPower + power, m_rightPower);
            } else {
                Robot.drive.setDrivePower(m_leftPower, m_rightPower - power);
            }
            yield();
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}