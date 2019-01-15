package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class PreciseDrive extends Subroutine {

    double m_driveTime;

    public PreciseDrive(double driveTime) {
        m_driveTime = driveTime;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double leftPower;
        double rightPower;
        int i = 0;
        int numUpdates = 1000;
        double currentAngle = Robot.drive.getGyroAngle();
        double error = 0;
        while (i < m_driveTime * numUpdates) {
            leftPower = 0.25;
            rightPower = 0.25;
            if (Math.round(Robot.drive.getGyroAngle()) != Math.round(currentAngle)) {
                error = Robot.drive.getGyroAngle() - currentAngle;
                rightPower *= Math.pow((error / 180), 2) + 1;
                leftPower *= Math.pow((error / 180) - 1, 2) + 1;
            }
            i++;
            Robot.drive.setDrivePower(leftPower, rightPower);
            waitForSeconds(1.0 / numUpdates);
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}