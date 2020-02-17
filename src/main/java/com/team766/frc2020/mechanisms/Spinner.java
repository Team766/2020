package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;


public class Spinner extends Mechanism {

    private CANSpeedController m_talon;

    public Spinner() {
        m_talon = RobotProvider.instance.getTalonCANMotor("spinner.talon");
    }

    public void setPower(double spinnerPower) {
        m_talon.set(spinnerPower);
    }
}