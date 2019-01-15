package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class PreciseTurn extends Subroutine {

    double m_turnAngle;
    double m_rampAngle;

    public PreciseTurn() {
        m_turnAngle = 90;
        m_rampAngle = 20;
        takeControl(Robot.drive);
    }

    public PreciseTurn(double turnAngle) {
        m_turnAngle = turnAngle;
        m_rampAngle = Math.max(turnAngle / 4.5, 20);
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double oldAngle = Robot.drive.getGyroAngle();
        double newAngle = oldAngle + m_turnAngle;
        double targetAngle;
        if(Math.abs(newAngle - oldAngle) < Math.abs(newAngle - 360 - oldAngle)) {
			targetAngle = newAngle - oldAngle; // No zero cross
        } else {
            targetAngle = newAngle - 360 - oldAngle; // Zero cross
        }
        double direction = 0;
        double power = 0;
        while (!(Robot.drive.getGyroAngle() >= newAngle - 1 && Robot.drive.getGyroAngle() <= newAngle + 1)) {
            if (targetAngle > oldAngle) {
                System.out.println("Current Angle: " + Robot.drive.getGyroAngle() + " Target Angle: " + newAngle);
                direction = -1;
            } else if (targetAngle < oldAngle) {
                System.out.println("Current Angle: " + Robot.drive.getGyroAngle() + " Target Angle: " + newAngle);
                direction = 1;
            }
            if (Robot.drive.getGyroAngle() - oldAngle < m_rampAngle - oldAngle || Robot.drive.getGyroAngle() - targetAngle < m_rampAngle - targetAngle) {
                power = 0.25;
            } else {
                power = 0.5;
            }
            Robot.drive.setDrivePower(power * direction, power * direction);
            yield();
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}