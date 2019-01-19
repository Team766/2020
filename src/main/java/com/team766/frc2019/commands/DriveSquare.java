package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveSquare extends Subroutine {
    public DriveSquare() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
            callSubroutine(new PreciseTurn(90)); 
            waitForSeconds(1.0);
            callSubroutine(new PreciseTurn(45)); 
            waitForSeconds(1.0);

            callSubroutine(new PreciseTurn(-45)); 
            waitForSeconds(1.0);

            callSubroutine(new PreciseTurn(-90)); 
            waitForSeconds(1.0);

    }  
}