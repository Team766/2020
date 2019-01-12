package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class PreciseTurn extends Subroutine {
    private double m_angle;

    public PreciseTurn(double angle) {
        m_angle = angle;
        takeControl(Robot.drive);

    }


    protected void subroutine() {
        double leftDrivePower;
        double rightDrivePower;
        if (m_angle > 0) {
            leftDrivePower = 1.0;
            rightDrivePower = -1.0;
        } else {
            leftDrivePower = -1.0;
            rightDrivePower = 1.0;
        }
        Robot.drive.setDrivePower(leftDrivePower, rightDrivePower);

       
        waitFor(() -> Robot.drive.getGyroAngle() >= m_angle);

        Robot.drive.setDrivePower(0.0, 0.0);
    }
} 