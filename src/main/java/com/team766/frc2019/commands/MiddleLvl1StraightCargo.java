package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class MiddleLvl1StraightCargo extends Subroutine {

    private LimePickup m_limePickup = new LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider());

    public MiddleLvl1StraightCargo() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto7 STARTING");
        Robot.drive.resetEncoders();
        Robot.drive.resetGyro();
        callSubroutine(new PreciseDrive(0, 10, .7, 0)); 
        m_limePickup.start();
       // callSubroutine(new PreciseTurn(180));
       // callSubroutine(new PreciseDrive(180, 10, .5, 0));
        
        System.out.println("DriveAuto7 IS DONE");
        Robot.drive.nukeRobot();
        yield();
    }
}
