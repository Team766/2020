package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;


public class WaterWheel extends Mechanism {

    private CANSpeedController m_talon;

    public WaterWheel() {
        m_talon = RobotProvider.instance.getTalonCANMotor("waterwheel.talon");
    }

    public void setPower(double wheelPower) {
        m_talon.set(wheelPower);
    }
}