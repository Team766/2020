package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class TurnLeft extends Subroutine {
    public TurnLeft() {
        takeControl(Robot.drive);
    }
    protected void subroutine(){
        Robot.drive.setDrivePower(0.5, -0.5);
        waitForSeconds(0.7);

    Robot.drive.setDrivePower(0, 0);
   
    }
}