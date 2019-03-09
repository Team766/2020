package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;

public class DriveAuto4 extends Subroutine {
    public DriveAuto4() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto7 STARTING");
        Robot.drive.resetEncoders();
        Robot.drive.resetGyro();

        callSubroutine(new PreciseDrive(0, 10, .5, 0));
        callSubroutine(new PreciseTurn(180));
        callSubroutine(new PreciseDrive(180, 10, .5, 0));
        
        System.out.println("DriveAuto7 IS DONE");
        Robot.drive.nukeRobot();
        yield();
    }
}
