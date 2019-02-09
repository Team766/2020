package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

public class DriveVelocity extends Subroutine {
    public DriveVelocity() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double dist = 0;
        for (int i = 0; i <= 100; i++) {
//            dist = i / Robot.drive.DIST_PER_PULSE;
            System.out.println("velocity: " + (i*1000.0));
            Robot.drive.setDrive(i*1000.0, i*1000.0, ControlMode.Velocity);
            waitForSeconds(2.0);
        }
        Robot.drive.setDrive(0.0, 0.0, ControlMode.PercentOutput);
    }
}
