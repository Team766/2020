package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.JoystickReader;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class RightLvl1RightSideCargo extends Subroutine {

    private JoystickReader m_joystick1  = RobotProvider.instance.getJoystick(1);
    private LimeScore m_limeScore = new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider());

    public RightLvl1RightSideCargo() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DriveAuto5 STARTING");
            Robot.drive.resetEncoders();
            Robot.drive.resetGyro();

           waitForSeconds(0.5);
        //    callSubroutine(new PreciseDrive(0, 15 , 0.9, 0.9));
            //callSubroutine(new PreciseTurn(30));
           //callSubroutine(new PreciseDrive(0, 2, 0.6, 0));
            //callSubroutine(new PreciseTurn(45));
           // callSubroutine(new PreciseTurn(0));
           // callSubroutine(new PreciseDrive(0, 3, 0.6, 0));
          //  callSubroutine(new PreciseTurn(180));
         // callSubroutine(new PreciseDrive(0, 3, 0.6, 0));
          callSubroutine(new PreciseTurnRadius(90, 2, 0.8, 0));



            //callSubroutine(new PreciseDrive(Robot.drive.getGyroAngle(), 2, 0.9, 0));

           // m_limeScore.start();

            //callSubroutine(new PreciseDrive(0, -2, .5, 0));
            
            System.out.println("Right cargo IS DONE");
            Robot.drive.nukeRobot();
            yield();
        }

}
