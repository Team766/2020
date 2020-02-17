package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.SolenoidController;


public class WaterWheel extends Mechanism {

    private CANSpeedController m_talon;
    private SolenoidController m_ballPusher;

    public WaterWheel() {
        m_talon = RobotProvider.instance.getTalonCANMotor("waterwheel.talon");
        m_ballPusher = RobotProvider.instance.getSolenoid("waterwheel.pusher");
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
}