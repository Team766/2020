package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;

public class DriveSquare extends Subroutine {
    public DriveSquare() {
        //takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DRIVESQUARE STARTING");

        Robot.drive.resetGyro();
        Robot.drive.resetEncoders();
        /*callSubroutine(new PreciseDrive(0, -5, .5, 0, .5));
        callSubroutine(new PreciseTurnRadius(90, 0, .5, .5, .5));
        callSubroutine(new PreciseTurnRadius(180, 0, .5, .5, .5));
        callSubroutine(new PreciseDrive(180, 5, .5, .5, 0)); dukes of hazzard turn*/
        /*callSubroutine(new PreciseDrive(0, 5, .5, .5));
        callSubroutine(new PreciseTurnRadius(90, 2, .5, .5));
        callSubroutine(new PreciseTurnRadius(180, 2, .5, .5));
        callSubroutine(new PreciseDrive(180, 5, .5, .5));
        callSubroutine(new PreciseDrive(180, -5, .5, .5));
        callSubroutine(new PreciseTurnRadius(90, -2, .5, .5));
        callSubroutine(new PreciseTurnRadius(0, -2, .5, .5));
        callSubroutine(new PreciseDrive(0, -5, .5, .5)); mcdonalds logo*/
        /*callSubroutine(new PreciseDrive(0, 10.4375, .5, 0));
        callSubroutine(new ExtendGripper());
        callSubroutine(new PreciseDrive(0, -1.666666666666667, .5, 0));
        callSubroutine(new RetractGripper());
        callSubroutine(new PreciseTurn(225));
        callSubroutine(new PreciseDrive(225, 14, .5, .5));
        callSubroutine(new PreciseTurnRadius(180, 0.6666666666666667, .5, .5));
        callSubroutine(new ExtendGripper());
        callSubroutine(new PreciseDrive(180, 4, .5, 0));
        callSubroutine(new RetractGripper());
        callSubroutine(new PreciseDrive(180, -4, .5, 0));*/
        callSubroutine(new PreciseTurnRadius(90, 4, .25, 0));
        /*while (Robot.drive.isEnabled()) {
            callSubroutine(new PreciseTurnRadius(270, 2, .5, 0, .5));
            //callSubroutine(new PreciseDrive(270, 1, .5, .5, .5));
            callSubroutine(new PreciseTurnRadius(180, 2, .5, .5, .5));
            //callSubroutine(new PreciseDrive(180, 1, .5, .5, .5));
            callSubroutine(new PreciseTurnRadius(90, 2, .5, .5, .5));
            //callSubroutine(new PreciseDrive(90, 1, .5, .5, .5));
            callSubroutine(new PreciseTurnRadius(0, 2, .5, .5, 0));
            //callSubroutine(new PreciseDrive(0, 1, .5, .5, .5));
            callSubroutine(new PreciseTurnRadius(90, 2, .5, 0, .5));
            //callSubroutine(new PreciseDrive(90, 1, .5, .5, .5));
            callSubroutine(new PreciseTurnRadius(180, 2, .5, .5, .5));
            //callSubroutine(new PreciseDrive(180, 1, .5, .5, .5));
            callSubroutine(new PreciseTurnRadius(270, 2, .5, .5, .5));
            //callSubroutine(new PreciseDrive(270, 1, .5, .5, .5));
            callSubroutine(new PreciseTurnRadius(0, 2, .5, .5, 0));
            //callSubroutine(new PreciseDrive(0, 1, .5, .5, .5));
        }*/
        /*callSubroutine(new PreciseDrive(4, 270, .25, .25, .25));
        waitForSeconds(2.0);
        callSubroutine(new PreciseTurnRadius(180, 2, .25, .25, .25));
        waitForSeconds(2.0);
        callSubroutine(new PreciseTurnRadius(90, 2, .25, .25, .25));
        waitForSeconds(2.0);
        callSubroutine(new PreciseTurnRadius(0, 2, .25, .25, .25));
        waitForSeconds(2.0);
        callSubroutine(new PreciseDrive(4, 0, .5, .5, .5));
        waitForSeconds(2.0);*/
        System.out.println("DRIVESQUARE IS DONE");
        Robot.drive.shutdown();
    }


        


       // double endTime = new Date().getTime();
        //System.out.println("ENDED IN " + (endTime - startTime));

    }
