package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;


public class TurnLeft extends Subroutine {
    private double m_angle;

    public TurnLeft(double angle) {
        m_angle = angle;
        takeControl(Robot.drive);

    }

    protected void subroutine() {
        Robot.drive.setDrivePower(1.0, 1.0);
        waitForSeconds(1.0);

        Robot.drive.setDrivePower(0.0, 0.0);
    }


} 