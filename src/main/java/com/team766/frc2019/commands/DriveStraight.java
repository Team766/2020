package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveStraight extends Subroutine {
    public DriveStraight(double distance) {
        takeControl(Robot.drive);
    }

    protected void subroutine() {

        Robot.drive.setDrivePower(0.5, 0.5);
        System.out.println("Encoder value is" );
        waitForSeconds(1.0);

        Robot.drive.setDrivePower(0.0, 0.0);
        System.out.println("Encoder value is" );
    }
    
}
