package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.SpeedController;

import edu.wpi.first.wpilibj.VictorSP;

import com.team766.hal.CANSpeedController;

public class Drive extends Mechanism {

    private VictorSP m_rightMotor;
    private VictorSP m_leftMotor;

    public Drive() {
        m_leftMotor = new VictorSP(7);
        m_rightMotor = new VictorSP(9);
    }

    public void setDrivePower(double leftPower, double rightPower) {
        m_leftMotor.set(leftPower);
        m_rightMotor.set(rightPower);
    }
}


