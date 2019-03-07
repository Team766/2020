package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;

public class DriveAuto4 extends Subroutine {
    public DriveAuto4() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto4 STARTING");
        Robot.drive.resetEncoders();
        Robot.drive.resetGyro();

        Robot.flowerGripper.extend();
        Robot.flowerActuator.retract();
        waitForSeconds(3.0);
        Robot.flowerGripper.retract();
        callSubroutine(new PreciseDrive(0, 6.01, .5, 0));
        System.out.println("first drive done");
        callSubroutine(new PreciseTurn(90));
        callSubroutine(new PreciseDrive(90, 2, .5, 0));
        callSubroutine(new PreciseTurn(0));
        callSubroutine(new PreciseDrive(0, 4.4, .5, 0));
        callSubroutine(new PreciseTurn(-90));
        callSubroutine(new PreciseDrive(-90, 2, .5, 0));
        Robot.flowerActuator.extend();
        Robot.flowerGripper.extend();
        /*//callSubroutine(new LimeLight.jpg);
        callSubroutine(new PreciseDrive(-90, -1, .5, 0));
        callSubroutine(new PreciseTurn(-200));
        callSubroutine(new PreciseDrive(-160, 10, .75, 0));
        callSubroutine(new PreciseTurn(-180));
        //callSubroutine(new LimeLight.jpg);
        callSubroutine(new PreciseTurn(-10));
        callSubroutine(new PreciseDrive(-10, 12, .75, 0));
        callSubroutine(new PreciseTurn(-90));
        //callSubroutine(new LimeLight.jpg);*/
        
        System.out.println("DriveAuto4 IS DONE");
        Robot.drive.nukeRobot();
        yield();
    }
}
