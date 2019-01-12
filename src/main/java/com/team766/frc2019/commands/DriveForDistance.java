package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public abstract class DriveForDistance extends Subroutine {
    public DriveForDistance() {
        takeControl(Robot.drive);
        takeControl(Robot.encoderRead);
        
    }

    protected abstract void subroutine();

    protected double db_testDistance = 10.0; //the distance in feet the robot drives in 10 seconds

    protected void driveByTime(double distance) {
        Robot.drive.setDrivePower(1.0,1.0);
        waitForSeconds((db_testDistance / 10) * distance);
        Robot.drive.setDrivePower(0.0,0.0);
    }

    protected void driveForTime(double time) {
        Robot.drive.setDrivePower(1.0,1.0);
        waitForSeconds(time);
        Robot.drive.setDrivePower(0.0,0.0);
    }

    protected void driveByEncoders(double distance) {
        while (b_driving) {
            
        }

    }
    

}