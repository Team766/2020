package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class PreciseDrive extends Subroutine {

    double m_driveTime;

    public PreciseDrive() {
        m_driveTime = 4.0;
        takeControl(Robot.drive);
    }

    public PreciseDrive(double driveTime) {
        m_driveTime = driveTime;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double leftPower;
        double rightPower;
        int i = 0;
        int numUpdates = 10;
        double targetAngle = Robot.drive.getGyroAngle();
        double error = 0;
        while (i < (m_driveTime * numUpdates)) {
            leftPower = 0.25;
            rightPower = 0.25;
            if (Math.round(Robot.drive.getGyroAngle()) != Math.round(targetAngle)) {
                if (Robot.drive.getGyroAngle() < targetAngle) {
                    if (Math.abs(Robot.drive.getGyroAngle() - targetAngle) < 180) {
                        error = Math.abs(Robot.drive.getGyroAngle() - targetAngle);
                    } else {
                        error = -Math.abs(Robot.drive.getGyroAngle() - targetAngle);
                    }
                } else {
                    if (Math.abs(Robot.drive.getGyroAngle() - targetAngle) < 180) {
                        error = -Math.abs(Robot.drive.getGyroAngle() - targetAngle);
                    } else {
                        error = Math.abs(Robot.drive.getGyroAngle() - targetAngle);
                    }
                }
                if (error > 0) {
                    rightPower *= Math.pow((error / 180), 2) + 1;
                } else {
                    leftPower *= Math.pow((error / 180), 2) + 1;
                }
            }
            System.out.println("(Going Straight) Current Angle: " + Robot.drive.getGyroAngle() + " Target Angle: " + targetAngle + " Left Power: " + leftPower + " Right Power: " + rightPower);
            i++;
            Robot.drive.setDrivePower(leftPower, rightPower);
            waitForSeconds(1.0 / numUpdates);
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}