package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveSquare extends Subroutine {
    public DriveSquare() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        for (int i = 0; i < 4; i++) {
            this.callSubroutine(new DriveStraight(3));
            this.callSubroutine(new TurnLeft(90)); 
        }
     
    }  
}