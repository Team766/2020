package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.SpeedController;

public class Drive extends Mechanism {

    private SpeedController m_leftMotor;
    private SpeedController m_rightMotor;

    public Drive() {
        m_leftMotor = RobotProvider.instance.getMotor("drive.leftMotor");
        m_rightMotor = RobotProvider.instance.getMotor("drive.rightMotor");
    }

    public void setDrivePower(double leftPower, double rightPower) {
        m_leftMotor.set(leftPower);
        m_rightMotor.set(rightPower);
    }
}