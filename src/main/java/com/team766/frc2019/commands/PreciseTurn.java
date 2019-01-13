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
        double newAngle = currentAngle + m_turnAngle;
        double turnAngle;
        if(Math.abs(newAngle - currentAngle) < Math.abs(newAngle - 360 - currentAngle) )
			turnAngle = newAngle - currentAngle; // No zero cross
		else
			turnAngle = newAngle - 360 - currentAngle; // Zero cross
        if (turnAngle > currentAngle) {
            while (Robot.drive.getGyroAngle() <= turnAngle) {
                System.out.println("Current Angle: " + Robot.drive.getGyroAngle() + " Target Angle: " + newAngle);
                Robot.drive.setDrivePower(-0.1, -0.1);
                yield();
            }
        } else if (turnAngle < currentAngle) {
            while (Robot.drive.getGyroAngle() >= turnAngle) {
                System.out.println("Current Angle: " + Robot.drive.getGyroAngle() + " Target Angle: " + newAngle);
                Robot.drive.setDrivePower(0.1, 0.1);
                yield();
            }
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}