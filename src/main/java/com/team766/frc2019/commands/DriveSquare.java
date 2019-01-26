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

        Robot.drive.resetGyro();
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
        }


        System.out.println("DRIVESQUARE IS DONE");


        double endTime = new Date().getTime();
        System.out.println("ENDED IN " + (endTime - startTime));

    }
}