package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class Debug extends Subroutine {
    public Debug() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        Robot.drive.resetEncoders();
        Robot.drive.resetGyro();
        Robot.drive.shutdown();
        while (Robot.drive.isEnabled()) {
            //System.out.println("Left Encoder: " + Robot.drive.leftEncoderDistance() + " Gyro: " + Robot.drive.getGyroAngle());
            System.out.println("Left Encoder: " + Robot.drive.leftEncoderDistance() + " Right Encoder: " + Robot.drive.rightEncoderDistance() + " Gyro: " + Robot.drive.getGyroAngle());
        }
    }
}
