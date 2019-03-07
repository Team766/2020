package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;

public class DriveAuto6 extends Subroutine {
    public DriveAuto6() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto6 STARTING");
        Robot.drive.resetEncoders();
        Robot.drive.resetGyro();

        Robot.flowerActuator.retract();
        Robot.flowerGripper.extend();
        callSubroutine(new PreciseDrive(0, 3, .5, .5));
        callSubroutine(new PreciseTurnRadius(30, 1, .6, .5));
        callSubroutine(new PreciseDrive(30, 6, .5, .5));
        callSubroutine(new PreciseTurnRadius(-90, 1, .6, 0));
        callSubroutine(new PreciseDrive(-90, 2, .4, 0));
        Robot.flowerActuator.extend();
        waitForSeconds(1.0);
        Robot.flowerGripper.retract();
        
        System.out.println("DriveAuto6 IS DONE");
        Robot.drive.nukeRobot();
        yield();
    }
}
