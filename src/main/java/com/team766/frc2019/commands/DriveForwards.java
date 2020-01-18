package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;

public class DriveForwards extends Subroutine {
    public DriveForwards() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DRIVEFORWARDS STARTING");

        Robot.drive.resetGyro();
        Robot.drive.resetEncoders();

        callSubroutine(new PreciseDrive(0.0, 10.0, 0.75, 0.0));
        callSubroutine(new PreciseTurn(125));
        callSubroutine(new PreciseDrive(125, 5.0, 0.75, 0.0));
        
        System.out.println("DRIVEFORWARDS IS DONE");
        Robot.drive.nukeRobot();
        return;
    }
}
