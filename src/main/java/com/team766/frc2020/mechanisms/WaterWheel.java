package com.team766.frc2020.mechanisms;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.team766.controllers.PIDController;
import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.DigitalInputReader;
import com.team766.hal.SolenoidController;
import com.team766.hal.CANSpeedController.ControlMode;

public class WaterWheel extends Mechanism {

    private CANSpeedController m_talon;
    private final SolenoidController m_ballPusher;
    private final CANSpeedController m_wheelMotor;
    private DigitalInputReader wheelLimitSwitch;
    PIDController m_velocityController = new PIDController(0.01, 0, 0, 10, RobotProvider.getTimeProvider());
    private double[] angles = {0.0, 72.0, 144.0, 216.0, 288.0};
    double feedforward = 0.07;

    
    public WaterWheel() {
        //m_talon = RobotProvider.instance.getTalonCANMotor("waterwheel.talon");
        m_ballPusher = RobotProvider.instance.getSolenoid("waterwheel.pusher");
        m_wheelMotor = RobotProvider.instance.getTalonCANMotor("waterwheel.motor");
        //wheelLimitSwitch = RobotProvider.instance.getDigitalInput("waterwheel.limitswitch");
    }

    public void setWheelPower(double wheelPower) {
        m_wheelMotor.set(wheelPower);
    }

    public void setMotionMagic(double targetPos) {
        m_wheelMotor.set(ControlMode.MotionMagic, targetPos);
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


    public void turnDegrees(int degrees) {
        int currentPosition = (int)(m_wheelMotor.getSensorPosition());
        m_wheelMotor.setPosition(currentPosition + degrees);
    }

}