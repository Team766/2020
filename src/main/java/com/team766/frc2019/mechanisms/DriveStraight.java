package com.team766.frc2019.mechanisms;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveStraight extends Subroutine {
    public DriveStraight() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        Robot.drive.setDriverPower(1.0, 1.0);
        waitForSeconds(1.0);

        Robot.drive.setDriverPower(0.0, 0.0);
    }
}