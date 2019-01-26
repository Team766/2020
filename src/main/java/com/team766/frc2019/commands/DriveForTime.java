package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveForTime extends Subroutine {
    final double TIME;
    final double POWER;
    public DriveForTime(Double time, double power) {
        TIME = time;
        POWER = power;
        takeControl(Robot.drive);
    } 
    
    protected void  subroutine() {
        Robot.drive.setDrivePower(POWER,POWER);
        waitForSeconds(TIME);
        Robot.drive.setDrivePower(0.0,0.0);
    }

}


