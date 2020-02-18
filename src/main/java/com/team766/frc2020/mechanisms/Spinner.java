package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.SolenoidController;

public class Spinner extends Mechanism {

    private CANSpeedController m_spinner;
    private SolenoidController m_actuator;
    private SolenoidController m_stopper;

    public Spinner() {
        m_spinner = RobotProvider.instance.getTalonCANMotor("spinner.talon");
        m_actuator = RobotProvider.instance.getSolenoid("spinner.actuator");
        m_stopper = RobotProvider.instance.getSolenoid("spinner.stopper");
    }

    public void setSpinnerPower(double spinnerPower) {
        m_spinner.set(spinnerPower);
    }

    public void setSpinnerState(boolean state) {
        m_actuator.set(state);
    }

    public void setStopperState(boolean state) {
        m_stopper.set(state);
    }
}