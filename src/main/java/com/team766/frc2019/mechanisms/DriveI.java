package com.team766.frc2019.mechanisms;

import com.team766.controllers.PIDController;
import com.team766.hal.CANSpeedController;

public interface DriveI {
    void setDrive(double leftSetting, double rightSetting);

    public double getDistPerPulse();

    boolean isEnabled();

    double getGyroAngle();

    void resetGyro();

    double leftEncoderDistance();

    double rightEncoderDistance();

    double getOutsideEncoderDistance(boolean turnDirection);

    void resetEncoders();

    void shutdown();

    boolean isTurnDone(PIDController turnController);

    double AngleDifference(double angle1, double angle2);

    void nukeRobot();

}
