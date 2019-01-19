package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveSquare extends Subroutine {
    public DriveSquare() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("DRIVESQUARE");
            callSubroutine(new PreciseTurn(-90)); 
            callSubroutine(new PreciseTurn(45)); 


    }  
}