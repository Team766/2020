package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController;
import com.team766.hal.DigitalInputReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.SolenoidController;
import com.team766.hal.CANSpeedController.ControlMode;

import edu.wpi.first.hal.DigitalGlitchFilterJNI;

public class Waterwheel extends Mechanism {

    private CANSpeedController m_wheelMotor;

    private DigitalInputReader wheelLimitSwitch;

    public Waterwheel() {
        //m_wheelMotor = RobotProvider.instance.getTalonCANMotor("");
        //wheelLimitSwitch = RobotProvider.instance.getDigitalInput("");
    }

    public double getWheelPosition() {
        return m_wheelMotor.getSensorPosition();
    }

    public double getWheelVelocity() {
        return m_wheelMotor.getSensorVelocity();
    }

    public void setWheelVelocity(double wheelVelocity) {
        m_wheelMotor.set(ControlMode.Velocity, wheelVelocity);
    }


}