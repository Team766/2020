package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class MiddleLvl1StraightCargoRight extends Subroutine {

    private LimePickup m_limePickup = new LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider());

    public MiddleLvl1StraightCargoRight() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto7 STARTING");
        Robot.drive.resetEncoders();
        Robot.drive.resetGyro();

        callSubroutine(new PreciseDrive(0, 8, 0.6, 0)); 
        callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        callSubroutine(new PreciseDrive(0, -3, 0.6, 0.6)); 
        callSubroutine(new PreciseDrive(90, -5, 0.6, 0.6)); 
        callSubroutine(new PreciseDrive(130, 14, 0.6, 0.6));
        callSubroutine(new PreciseTurnRadius(180 , 3, 0.6, 0.6)); 
        callSubroutine(new PreciseDrive(180, 3, 0.6, 0.6));
        callSubroutine(new LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        callSubroutine(new PreciseDrive(180, -3, 0.6, 0));
        callSubroutine(new TeleopAuton());
        
        System.out.println("DriveAuto7 IS DONE");
        Robot.drive.nukeRobot();
        yield();
    }
}

/*
callSubroutine(new PreciseDrive(0, 8, 0.6, 0)); 
callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
callSubroutine(new PreciseTurn(230)); 
//callSubroutine(new PreciseDrive(0, -3, 0.6, 0.6)); 
// callSubroutine(new PreciseTurnRadius(230, 2, -0.6, -0.6));
callSubroutine(new PreciseDrive(230, 10, 0.6, 0.6));
callSubroutine(new PreciseTurnRadius(0, 2, 0.6, 0.6)); 
waitForSeconds(3.0);
// callSubroutine(new LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
callSubroutine(new PreciseDrive(0, -3, 1.0, 0));
callSubroutine(new TeleopAuton()); 
*/