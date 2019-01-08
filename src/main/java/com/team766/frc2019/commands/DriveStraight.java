package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveStraight extends Subroutine {
    public DriveStraight() {
        takeControl(Robot.drive);
    }
    
    protected void subroutine() {
        Robot.drive.setDrivePower(1.0,1.0);
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                Robot.drive.setDrivePower(0.0,0.0);
            }
        }, 
        1000 
        );
    }
}
