package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveStraight extends Subroutine {

    //double m_driveTime;

    /*public DriveStraight(double driveTime) {
        m_driveTime = driveTime;
        takeControl(Robot.drive);
    }*/

    /*public DriveStraight() {
        takeControl(Robot.drive);
    }*/

    protected void subroutine() {
        Robot.drive.setDrivePower(-0.25, 0.25);
        //waitForSeconds(m_driveTime);
        waitForSeconds(3.0);

        Robot.drive.setDrivePower(0.0, 0.0);
    }
}