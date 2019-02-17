package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

public class Debug extends Subroutine {
    public Debug() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        Robot.drive.resetEncoders();
        Robot.drive.resetGyro();
        Robot.drive.shutdown();
        while (Robot.drive.isEnabled()) {
            //Robot.drive.setDrive(0.5, 0.5, ControlMode.PercentOutput);
            System.out.println("Left Encoder: " + Robot.drive.leftEncoderDistance() + " Right Encoder: " + Robot.drive.rightEncoderDistance() + " gyro: " + Robot.drive.getGyroAngle());
        }
    }
}
