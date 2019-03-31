package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.JoystickReader;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class LeftHab2SideCargoPlayerStationSideCargo extends Subroutine {


    public LeftHab2SideCargoPlayerStationSideCargo() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto5 STARTING");
            Robot.drive.resetEncoders();
            Robot.drive.resetGyro();

        waitForSeconds(0.3);
        callSubroutine(new PreciseDrive(0, 12 , 0.6, 0.0));
        callSubroutine(new PreciseTurnRadius(345, 2 ,0.6 , 0.6));
        callSubroutine(new PreciseDrive(345, 19 , 0.6, 0.6));
        callSubroutine(new PreciseTurnRadius(90, 2, 0.6, 0));
        callSubroutine(new PreciseDrive(90, -3, 0.6, 0));
        callSubroutine(new PreciseDrive(90, 1.5, 0.5, 0));
        callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        waitForSeconds(0.1);
        callSubroutine(new PreciseDrive(90, -3.5, 0.6, 0));
        //callSubroutine(new PreciseDrive(200, -3, 0.6, 0));
        callSubroutine(new PreciseDrive(200, 15, 0.6, 0));
        callSubroutine(new LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        callSubroutine(new PreciseDrive(180, -3, 0.6, 0.0));
        callSubroutine(new PreciseDrive(190, -25, 0.6, 0));
        /*
        callSubroutine(new PreciseDrive(185, 3, 0.6, 0.6));
        callSubroutine(new PreciseTurnRadius(90, 2, 0.6, 0));
        callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        waitForSeconds(0.3);
        callSubroutine(new PreciseDrive(90, -3, 0.6, 0));
        callSubroutine(new PreciseDrive(180, -3, 0.6, 0));
        callSubroutine(new PreciseDrive(190, 15, 0.6, 0));
        */
        callSubroutine(new TeleopAuton());

        
        System.out.println("Right cargo IS DONE");
        Robot.drive.nukeRobot();
        yield();
        }


}

/*
        waitForSeconds(0.3);
        callSubroutine(new PreciseDrive(0, 6 , 0.6, 0.0));
        callSubroutine(new PreciseTurnRadius(345, 2 ,0.6 , 0.6));
        callSubroutine(new PreciseDrive(345, 15, 0.6, 0.6));
        callSubroutine(new PreciseTurnRadius(90, 2, 0.6, 0));
        callSubroutine(new PreciseDrive(90, 1, 0.6, 0));
        callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        callSubroutine(new PreciseDrive(90, -2, 0.6, 0));
        callSubroutine(new PreciseTurn(190));
        callSubroutine(new PreciseDrive(190, 15, 0.6, 0));
        callSubroutine(new LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        callSubroutine(new PreciseDrive(190, -3, 0.6, 0));
        callSubroutine(new PreciseDrive(190, -20, 0.6, 0.6));
        callSubroutine(new PreciseTurn(90));
        callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        waitForSeconds(0.3);
        callSubroutine(new PreciseDrive(90, -3, 0.6, 0));
        callSubroutine(new PreciseTurn(190));
        callSubroutine(new PreciseDrive(190, 15, 0.6, 0));
        */
