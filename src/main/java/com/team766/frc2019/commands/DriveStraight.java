package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

public class DriveStraight extends Subroutine {
    public DriveStraight() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {

        Robot.drive.setDrive(0.5, 0.5, ControlMode.PercentOutput);
        System.out.println("Encoder value is" );
        waitForSeconds(1.0);

        Robot.drive.setDrive(0.0, 0.0, ControlMode.PercentOutput);
        System.out.println("Encoder value is" );
    }
    
}
