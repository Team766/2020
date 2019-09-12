package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

public class DrivePercent extends Subroutine {
    public DrivePercent() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {

        System.out.println("0.025");
        Robot.drive.setDrive(0.025, 0.025);
        waitForSeconds(5.0);
        System.out.println("0.05");
        Robot.drive.setDrive(0.05, 0.05);
        waitForSeconds(5.0);
        System.out.println("0.1");
        Robot.drive.setDrive(0.1, 0.1);
        waitForSeconds(5.0);
        System.out.println("0.25");
        Robot.drive.setDrive(0.25, 0.25);
        waitForSeconds(5.0);
        System.out.println("0.5");
        Robot.drive.setDrive(0.5, 0.5);
        waitForSeconds(5.0);
        System.out.println("1.0");
        Robot.drive.setDrive(1.0, 1.0);
        waitForSeconds(5.0);
        Robot.drive.setDrive(0.0, 0.0);
    }
}
