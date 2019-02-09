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
        //callSubroutine(new PreciseDrive(4, 0, .25, 0, .25));
        //while (Robot.drive.isEnabled()) {
            callSubroutine(new PreciseTurnRadius(90, 2, .25, .25, .25));
            waitForSeconds(2.0);
            callSubroutine(new PreciseTurnRadius(180, 2, .25, .25, .25));
            waitForSeconds(2.0);
            callSubroutine(new PreciseTurnRadius(270, 2, .25, .25, .25));
            waitForSeconds(2.0);
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
        //}
        System.out.println("DRIVESQUARE IS DONE");
        Robot.drive.shutdown();
    }


        


       // double endTime = new Date().getTime();
        //System.out.println("ENDED IN " + (endTime - startTime));

    }
