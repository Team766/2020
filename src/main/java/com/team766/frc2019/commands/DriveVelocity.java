package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

public class DriveVelocity extends Subroutine {
    public DriveVelocity() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double positions = 1000;
        double time = 5.0;
        System.out.println("velocity: " + positions * time + " encoder distance: " + (Robot.drive.leftEncoderDistance() + Robot.drive.rightEncoderDistance()) / 2.0);
        Robot.drive.setDrive(positions * 10, positions * 10, ControlMode.Velocity);
        waitForSeconds(time);
        Robot.drive.setDrive(0.0, 0.0, ControlMode.PercentOutput);
    }
}
