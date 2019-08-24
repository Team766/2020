package com.team766.frc2019.mechanisms;

import com.team766.controllers.PIDController;
import com.team766.frc2019.TestState;
import com.team766.hal.CANSpeedController;
import org.junit.Test;

public class DriveIMock implements DriveI {
    final TestState testState;

    public DriveIMock(TestState testState) {
        this.testState = testState;
    }

    @Override
    public void setDrive(double leftSetting, double rightSetting) {

    }

    @Override
    public double getDistPerPulse() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public double getGyroAngle() {
        return 0;
    }

    @Override
    public void resetGyro() {

    }

    @Override
    public double leftEncoderDistance() {
        return 0;
    }

    @Override
    public double rightEncoderDistance() {
        return 0;
    }

    @Override
    public double getOutsideEncoderDistance(boolean turnDirection) {
        return 0;
    }

    @Override
    public void resetEncoders() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isTurnDone(PIDController turnController) {
        return false;
    }

    @Override
    public double AngleDifference(double angle1, double angle2) {
        return 0;
    }

    @Override
    public void nukeRobot() {

    }
}
