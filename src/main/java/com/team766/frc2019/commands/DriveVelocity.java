package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveVelocity extends Subroutine {

    double index;
    double velocity;

    public DriveVelocity() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        /*Robot.drive.setDrive(0.0, 0.0);
        index = 0;
        velocity = 0;
        while (Robot.drive.isEnabled()) {
            Robot.drive.setDrive(velocity, velocity);
            if (index % 1000 == 0) {
                System.out.println("Velocity: " + velocity);
            }
            index++;
            index = index % 100;
            velocity++;
    }*/
        Robot.drive.setDrive(1.0, 1.0);
    }
}
