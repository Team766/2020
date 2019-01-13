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
        int numUpdates = 10;
        double currentAngle = Robot.drive.getGyroAngle();
        while (i < m_driveTime * numUpdates) {
            leftPower = 0.25;
            rightPower = 0.25;
            if (Robot.drive.getGyroAngle() > currentAngle + 2) {
                rightPower += 0.005 * currentAngle;
            } else if (Robot.drive.getGyroAngle() < currentAngle - 2) {
                leftPower += 0.005 * currentAngle;
            }
            i++;
            Robot.drive.setDrivePower(leftPower, rightPower);
            waitForSeconds(1.0 / numUpdates);
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}