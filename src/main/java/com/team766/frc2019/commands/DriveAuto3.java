package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;

public class DriveAuto3 extends Subroutine {
    public DriveAuto3() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DRIVESQUARE STARTING");
        Robot.drive.nukeRobot();

        callSubroutine(new PreciseDrive(0, 6.01, .75, .5));
        callSubroutine(new PreciseTurnRadius(-90, 2.16, .75, .75));
        callSubroutine(new PreciseTurnRadius(0, 2.24, .75, .75));
        callSubroutine(new PreciseDrive(0, 2.0, .75, .75));
        callSubroutine(new PreciseTurnRadius(90, 2.24, .75, .75));
        callSubroutine(new PreciseDrive(90, 1, .75, 0));
        //callSubroutine(new LimeLight.jpeg);
        callSubroutine(new ExtendGripper());
        callSubroutine(new PreciseDrive(90, -1, .75, .75));
        callSubroutine(new PreciseTurnRadius(0, 3, -.75, -.75));
        callSubroutine(new PreciseDrive(0, -5, .75, .75));
        callSubroutine(new PreciseTurnRadius(90, 3, -.75, -.75));
        callSubroutine(new PreciseTurnRadius(0, 3, -.75, -.75));
        callSubroutine(new PreciseDrive(0, -3.5, .75, 0));
        callSubroutine(new PreciseTurn(180));
        callSubroutine(new ExtendGripper());
        callSubroutine(new PreciseDrive(180, 3.5, .75, 0));
        //callSubroutine(new LimeLight.jpeg);
        callSubroutine(new RetractGripper());
        callSubroutine(new PreciseDrive(180, -3.5, .75, 0));
        callSubroutine(new PreciseTurn(0));
        callSubroutine(new PreciseDrive(0, 3.5, .75, .75));
        callSubroutine(new PreciseTurnRadius(90, 3, .75, .75));
        callSubroutine(new PreciseTurnRadius(0, 3, .75, .75));
        callSubroutine(new PreciseDrive(0, 6.666666667, .75, .75));
        callSubroutine(new PreciseTurnRadius(90, 3, .75, 0));
        //callSubroutine(new LimeLight.jpeg);
        callSubroutine(new ExtendGripper());
        
        
        System.out.println("DRIVESQUARE IS DONE");
        Robot.drive.nukeRobot();
        yield();
    }
}
