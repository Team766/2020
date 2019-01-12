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
        while (Robot.drive.getGyroAngle() != newAngle) {
            System.out.println("Current Angle: " + Robot.drive.getGyroAngle() + "\nTarget Angle: " + newAngle);
            if (newAngle > currentAngle) {
                Robot.drive.setDrivePower(0.25, 0.25);
            } else if (newAngle < currentAngle) {
                Robot.drive.setDrivePower(-0.25, -0.25);
            }
        }
        Robot.drive.setDrivePower(0.0, 0.0);
        /*
        if (newAngle > currentAngle) {
            Robot.drive.setDrivePower(0.25, 0.25);
        } else if (newAngle < currentAngle) {
            Robot.drive.setDrivePower(-0.25, -0.25);
        }
        waitFor(() -> (!((Robot.drive.getGyroAngle() <= newAngle + 5) && (Robot.drive.getGyroAngle() >= newAngle - 5))));*/
    }
}