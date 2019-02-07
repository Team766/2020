package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

public class DriveVelocity extends Subroutine {
    public DriveVelocity() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {

        System.out.println("0.5");
        Robot.drive.setDrive(0.5, 0.5, ControlMode.Velocity);
        waitForSeconds(5.0);
        System.out.println("1.0");
        Robot.drive.setDrive(1.0, 1.0, ControlMode.Velocity);
        waitForSeconds(5.0);
        System.out.println("5.0");
        Robot.drive.setDrive(5.0, 5.0, ControlMode.Velocity);
        waitForSeconds(5.0);
        System.out.println("10.0");
        Robot.drive.setDrive(10.0, 10.0, ControlMode.Velocity);
        waitForSeconds(5.0);
        System.out.println("50.0");
        Robot.drive.setDrive(50.0, 50.0, ControlMode.Velocity);
        waitForSeconds(5.0);
        System.out.println("100.0");
        Robot.drive.setDrive(100.0, 100.0, ControlMode.Velocity);
        waitForSeconds(5.0);
        Robot.drive.setDrive(0.0, 0.0, ControlMode.Velocity);
    }
    
}
