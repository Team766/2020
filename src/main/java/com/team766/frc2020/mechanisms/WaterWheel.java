package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.DigitalInputReader;
import com.team766.hal.SolenoidController;
import com.team766.hal.CANSpeedController.ControlMode;


public class WaterWheel extends Mechanism {

    private CANSpeedController m_talon;
    private SolenoidController m_ballPusher;

    private CANSpeedController m_wheelMotor;
    private DigitalInputReader wheelLimitSwitch;

    public WaterWheel() {
        m_talon = RobotProvider.instance.getTalonCANMotor("waterwheel.talon");
        m_ballPusher = RobotProvider.instance.getSolenoid("waterwheel.pusher");
        m_wheelMotor = RobotProvider.instance.getTalonCANMotor("waterwheel.motor");
        wheelLimitSwitch = RobotProvider.instance.getDigitalInput("waterwheel.limitswitch");
    }

    public void setPower(double wheelPower) {
        m_talon.set(wheelPower);
    }

    public void pushBall() {
        m_ballPusher.set(true);
    }

    public void retractPusher() {
        m_ballPusher.set(false);
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