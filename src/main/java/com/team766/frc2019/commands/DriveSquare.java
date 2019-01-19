package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.RobotProvider;
import java.util.Date;

public class DriveSquare extends Subroutine {
    public DriveSquare() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double startTime = new Date().getTime();
        System.out.println("DRIVESQUARE STARTING");
        callSubroutine(new PreciseTurn(-20)); 
        System.out.println("DRIVESQUARE IS DONE");
        //callSubroutine(new PreciseTurn(90));

        double endTime = new Date().getTime();
        System.out.println("ENDED IN" + (endTime - startTime));
        // callSubroutine(new PreciseTurn(45)); 


    }  
}