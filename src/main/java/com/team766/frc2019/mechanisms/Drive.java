package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.SpeedController;
import com.team766.hal.CANSpeedController;

public class Drive extends Mechanism {

    private CANSpeedController m_rightMotor;
    private CANSpeedController m_leftMotor;

    public Drive() {
        m_leftMotor = RobotProvider.instance.getTalonCANMotor("drive.leftMotor");
        m_rightMotor = RobotProvider.instance.getTalonCANMotor("drive.rightMotor");
    }

    public void setDrivePower(double leftPower, double rightPower) {
        m_leftMotor.set(leftPower);
        m_rightMotor.set(rightPower);
    }

    public void arcadeDrive(double fwdPower, double turnPower) {
        double maximum = Math.max(Math.abs(fwdPower), Math.abs(turnPower));
        double total = fwdPower + turnPower;
        double difference = fwdPower - turnPower;

        if (fwdPower >= 0) {
            if (turnPower >= 0) {
                m_leftMotor.set(maximum);
                m_rightMotor.set(difference);
            } else {
                m_leftMotor.set(total);
                m_rightMotor.set(maximum);
            }
        } else {
            if (turnPower >= 0) {
                m_leftMotor.set(total);
                m_rightMotor.set(-maximum);
            } else {
                m_leftMotor.set(-maximum);
                m_rightMotor.set(difference);
            }
        }
    }

}


