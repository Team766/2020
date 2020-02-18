package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.DigitalInputReader;
import com.team766.hal.SolenoidController;
import com.team766.hal.CANSpeedController.ControlMode;

public class Waterwheel extends Mechanism {

    private CANSpeedController m_talon;
    private SolenoidController m_ballPusher;

    private CANSpeedController m_wheelMotor;
    private DigitalInputReader wheelLimitSwitch;

    public Waterwheel() {
        m_talon = RobotProvider.instance.getTalonCANMotor("waterwheel.talon");
        m_ballPusher = RobotProvider.instance.getSolenoid("waterwheel.pusher");
        m_wheelMotor = RobotProvider.instance.getTalonCANMotor("waterwheel.motor");
        wheelLimitSwitch = RobotProvider.instance.getDigitalInput("waterwheel.limitswitch");
    }

    public void setWheelPower(double wheelPower) {
        m_talon.set(wheelPower);
    }

    public void setPusherState(boolean state) {
        m_ballPusher.set(state);
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