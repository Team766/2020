package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.GyroReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.SpeedController;


public class Drive extends Mechanism {

    private SpeedController m_leftMotor;
    private SpeedController m_rightMotor;
    private GyroReader m_gyro;

    public Drive() {
        m_leftMotor = RobotProvider.instance.getMotor("drive.leftMotor");
        m_rightMotor = RobotProvider.instance.getMotor("drive.rightMotor");
        m_gyro = RobotProvider.instance.getGyro("drive.gyro");
    }

    public void setDrivePower(double leftPower, double rightPower) {
        m_leftMotor.set(leftPower);
        m_rightMotor.set(rightPower);
    }

    public double getGyroAngle() {
        return(m_gyro.getAngle());
    }
}