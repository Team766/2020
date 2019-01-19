package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.RobotProvider;

public class DriveSquare extends Subroutine {
    public DriveSquare() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double startTime = RobotProvider.instance.getClock().getTime();
        System.out.println("PRECISETURN");
        callSubroutine(new PreciseTurn(-90)); 
        System.out.println("TURN IS DONE");
        callSubroutine(new PreciseTurn(90));

        double endTime = RobotProvider.instance.getClock().getTime();
        System.out.println("ENDED IN" + (endTime - startTime));
        // callSubroutine(new PreciseTurn(45)); 


    }  
}