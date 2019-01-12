package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class PreciseTurnInertia extends Subroutine {
    private double m_angle = 90;
    private static double RAMP_ANGLE = 20;


    public PreciseTurnInertia(double angle) {
        m_angle = angle;
        takeControl(Robot.drive);

    }


    protected void subroutine() {
        double leftDrivePower;
        double rightDrivePower;
        double speedForce = 0.25;
        if (m_angle > 0) {
            leftDrivePower = 1.0;
            rightDrivePower = -1.0;
        } else {
            leftDrivePower = -1.0;
            rightDrivePower = 1.0;
        }
        Robot.drive.setDrivePower(leftDrivePower * speedForce, rightDrivePower * speedForce);

       
        while (Robot.drive.getGyroAngle() < m_angle) {
            double currentAngle = Robot.drive.getGyroAngle();
            if (currentAngle < RAMP_ANGLE) {
                speedForce = currentAngle/ RAMP_ANGLE;
            } else if (currentAngle < m_angle-RAMP_ANGLE) { 
                speedForce = 1;
            } else {
                speedForce = 1 - (currentAngle - (m_angle-RAMP_ANGLE))/RAMP_ANGLE;
            } 
            Robot.drive.setDrivePower(leftDrivePower * speedForce, rightDrivePower * speedForce);
            yield();
        }

        Robot.drive.setDrivePower(0.0, 0.0);
    }
} 