package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;
import java.util.Date;

public class DriveSquare extends Subroutine {
    public DriveSquare() {
        //takeControl(Robot.drive);
    }

    protected void subroutine() {
        double startTime = new Date().getTime();
        System.out.println("DRIVESQUARE STARTING");

        //for (int i=1; i<5; i++) {
            //System.out.println("Entering DriveSquare loop # " + i);
            //callSubroutine(new PreciseDrive(2.5, 0.75, 0.75)); 
        callSubroutine(new PreciseTurn(90));
        //}

        System.out.println("DRIVESQUARE IS DONE");


        double endTime = new Date().getTime();
        System.out.println("ENDED IN " + (endTime - startTime));

    }
}