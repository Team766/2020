package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class PreciseTurn extends Subroutine {

    double m_turnAngle;

    public PreciseTurn(double turnAngle) {
        m_turnAngle = turnAngle;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double currentAngle = Robot.drive.getGyroAngle();
        double newAngle = (currentAngle + m_turnAngle) % 360;
        if (newAngle > currentAngle) {
            while (Robot.drive.getGyroAngle() <= newAngle) {
                System.out.println("Current Angle: " + Robot.drive.getGyroAngle() + "Target Angle: " + newAngle);
                Robot.drive.setDrivePower(-0.25, -0.25);
            }
        } else if (newAngle < currentAngle) {
            while (Robot.drive.getGyroAngle() >= newAngle) {
                System.out.println("Current Angle: " + Robot.drive.getGyroAngle() + "Target Angle: " + newAngle);
                Robot.drive.setDrivePower(0.25, 0.25);
            }
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}