package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.JoystickReader;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class LeftHab2RocketPlayerStationBackRocket extends Subroutine {

    private JoystickReader m_joystick1  = RobotProvider.instance.getJoystick(1);

    public LeftHab2RocketPlayerStationBackRocket() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto5 STARTING");
            Robot.drive.resetEncoders();
            Robot.drive.resetGyro();

        waitForSeconds(0.5);
        callSubroutine(new PreciseDrive(0, 8, 1.0, 0));
        callSubroutine(new PreciseTurnRadius(330, 2 ,1.0 , 0));

       // callSubroutine(new PreciseTurn(330));
        callSubroutine(new PreciseDrive(330, 16, 1.0, 0));
        callSubroutine( new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        //waitForSeconds(1.0);
        callSubroutine(new PreciseDrive(330, -3 , 1.0, 0));
        //callSubroutine(new PreciseTurnRadius(180, 4 , -0.9 , 0));
        callSubroutine(new PreciseTurn(195));
        callSubroutine(new PreciseDrive(195, 13 , 1.0, 0));
        callSubroutine( new  LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        callSubroutine(new PreciseDrive(193, -3 , 1.0, 0));
       // callSubroutine(new PreciseTurn(10));
        callSubroutine(new PreciseDrive(193, -65, 1.0, 0));
        callSubroutine(new PreciseTurn(229));
        callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        waitForSeconds(0.5);
        callSubroutine(new PreciseDrive(229, -3 , 1.0, 0));
        //callSubroutine(new PreciseTurn(2));

    

            System.out.println("Right cargo IS DONE");
            Robot.drive.nukeRobot();
            yield();
        }

}
