package com.team766.frc2019.commands;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveForward extends Subroutine {
    public DriveForward(){
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        // nothing yet
    }
}