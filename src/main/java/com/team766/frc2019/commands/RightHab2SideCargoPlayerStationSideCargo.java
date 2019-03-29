package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.JoystickReader;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class RightHab2SideCargoPlayerStationSideCargo extends Subroutine {


    public RightHab2SideCargoPlayerStationSideCargo() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto5 STARTING");
            Robot.drive.resetEncoders();
            Robot.drive.resetGyro();

            waitForSeconds(0.3);
            callSubroutine(new PreciseDrive(0, 8 , 1.0, 0.0));
            callSubroutine(new PreciseTurnRadius(35, 2 ,1.0 , 0));
            callSubroutine(new PreciseDrive(35, 20, 1.0, 0));
            callSubroutine(new PreciseTurn(90));
            callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
            callSubroutine(new PreciseDrive(90, -3, 1.0, 0));
            callSubroutine(new PreciseTurn(190));
            callSubroutine(new PreciseDrive(190, 15, 1.0, 0));
            callSubroutine(new  LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
            callSubroutine(new PreciseDrive(190, -3, 1.0, 0));
            callSubroutine(new PreciseTurnRadius(185, 2 ,1.0 , 1.0));
            callSubroutine(new PreciseDrive(190, -20, 1.0, 0));
            callSubroutine(new PreciseTurn(190));
            callSubroutine(new PreciseTurn(90));
            callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
            callSubroutine(new PreciseDrive(190, -3, 1.0, 0));
            callSubroutine(new PreciseTurn(190));
            callSubroutine(new PreciseDrive(190, 15, 1.0, 0));
        
            System.out.println("Right cargo IS DONE");
            Robot.drive.nukeRobot();
            yield();
        }


}
