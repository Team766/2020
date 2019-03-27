package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;

public class DriveAuto2 extends Subroutine {
    public DriveAuto2() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DRIVESQUARE STARTING");
        Robot.drive.nukeRobot();

        callSubroutine(new PreciseDrive(0, 4, .9, .9));
        callSubroutine(new PreciseDrive(40 , 4, .9, .9));

        


        
        System.out.println("DRIVESQUARE IS DONE");
        Robot.drive.nukeRobot();
        yield();
    }
}
