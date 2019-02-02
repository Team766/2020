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
        //%callSubroutine(new PreciseDrive(10, 0, .25, 0, 0));
        callSubroutine(new PreciseTurnRadius(90, 0, .25, .25, .25));
        //callSubroutine(new PreciseDrive(2, 90, .5, 0, .25));
        //callSubroutine(new PreciseTurnRadius(180, 1, .25, .25, .25));
        //callSubroutine(new PreciseDrive(2, 180, .5, 0, .25));
        //callSubroutine(new PreciseTurnRadius(270, 1, .25, .25, .25));
        //callSubroutine(new PreciseDrive(2, 270, .5, 0, .25));
        //callSubroutine(new PreciseTurnRadius(0, 1, .25, .25, .25));
        /*
        for (int i = 0; i < 2; i++) {
           callSubroutine(new PreciseDrive(1, 0, 0.5, 0.5)); 
            callSubroutine(new PreciseTurn(90));
            callSubroutine(new PreciseDrive(1, 90, 0.5, 0.5)); 
            callSubroutine(new PreciseTurn(180));
            callSubroutine(new PreciseDrive(1, 180, 0.5, 0.5)); 
            callSubroutine(new PreciseTurn(270));
            callSubroutine(new PreciseDrive(1, 270, 0.5, 0.5)); 
            callSubroutine(new PreciseTurn(0));
            callSubroutine(new PreciseDrive(1, 0, 0.5, 0.5)); 
            callSubroutine(new PreciseTurn(0));
        */
            System.out.println("DRIVESQUARE IS DONE");
        }


        


       // double endTime = new Date().getTime();
        //System.out.println("ENDED IN " + (endTime - startTime));

    }
