package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.SolenoidController;

public class Spinner extends Mechanism {

    private CANSpeedController m_spinner;
    private SolenoidController m_actuator;

    public Spinner() {
        m_spinner = RobotProvider.instance.getVictorCANMotor("spinner.victor");
        m_actuator = RobotProvider.instance.getSolenoid("spinner.actuator");
    }

    public void setSpinnerPower(double spinnerPower) {
        m_spinner.set(spinnerPower);
    }

    public void setSpinnerState(boolean state) {
        m_actuator.set(state);
    }

}