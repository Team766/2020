package com.team766.frc2019.commands;

import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class TurnLeft extends Subroutine {

    double m_turnTime;

    public TurnLeft() {
        m_turnTime = 1.0;
        takeControl(Robot.drive);
    }

    public TurnLeft(double turnTime) {
        m_turnTime = turnTime;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        Robot.drive.setDrive(0.25, 0.25);
        //waitForSeconds(m_turnTime);
        waitForSeconds(1.0);

        Robot.drive.setDrive(0.0, 0.0);
    }
}