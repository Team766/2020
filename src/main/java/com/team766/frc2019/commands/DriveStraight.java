package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

public class DriveStraight extends Subroutine {
    public DriveStraight() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        callSubroutine(new PreciseDrive(0, 5, .5, 0));
    }
    
}
