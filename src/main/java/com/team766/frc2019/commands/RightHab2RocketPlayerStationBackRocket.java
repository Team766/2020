package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.JoystickReader;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class RightHab2RocketPlayerStationBackRocket extends Subroutine {

    private JoystickReader m_joystick1  = RobotProvider.instance.getJoystick(1);

    public RightHab2RocketPlayerStationBackRocket() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto5 STARTING");
            Robot.drive.resetEncoders();
            Robot.drive.resetGyro();

        callSubroutine(new PreciseDrive(0, 8, 0.5, 0));
        callSubroutine(new PreciseTurnRadius(90, 2 , 0.5 , 0));
        callSubroutine(new PreciseDrive(90, 3, 0.5, 0));


            /*
        waitForSeconds(0.3);
        callSubroutine(new PreciseDrive(0, 8, 1.0, 0));
        callSubroutine(new PreciseTurnRadius(35, 2 ,1.0 , 0));
        callSubroutine(new PreciseDrive(35, 7, 1.0, 0));
        callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        callSubroutine(new PreciseDrive(30, -3 , 1.0, 0));
        callSubroutine(new PreciseTurn(165));
        callSubroutine(new PreciseDrive(165, 10 , 1.0, 0));
        callSubroutine(new LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        callSubroutine(new PreciseDrive(168, -3 , 1.0, 0));
        callSubroutine(new PreciseDrive(168, -20, 1.0, 0));
        callSubroutine(new PreciseTurn(131));
        callSubroutine(new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
        waitForSeconds(0.3);
        callSubroutine(new PreciseDrive(131, -3 , 1.0, 0));
        */
    

            System.out.println("Right cargo IS DONE");
            Robot.drive.nukeRobot();
            yield();
        }

}
