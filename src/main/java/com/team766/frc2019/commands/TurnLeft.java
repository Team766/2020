package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class TurnLeft extends Subroutine {

    double m_turnTime;

    public TurnLeft(double turnTime) {
        m_turnTime = turnTime;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        Robot.drive.setDrivePower(1.0, 1.0);
        waitForSeconds(m_turnTime);

        Robot.drive.setDrivePower(0.0, 0.0);
    }
}