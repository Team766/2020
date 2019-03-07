package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;

public class DriveAuto5 extends Subroutine {
    public DriveAuto5() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto5 STARTING");
        Robot.drive.resetEncoders();
        Robot.drive.resetGyro();

        Robot.flowerActuator.retract();
        callSubroutine(new ExtendGripper());
        callSubroutine(new PreciseDrive(0, 8, .5, 0));
        Robot.flowerActuator.extend();
        callSubroutine(new RetractGripper());
        callSubroutine(new PreciseDrive(0, -6, .5, 0));
        
        System.out.println("DriveAuto5 IS DONE");
        Robot.drive.nukeRobot();
        yield();
    }
}
