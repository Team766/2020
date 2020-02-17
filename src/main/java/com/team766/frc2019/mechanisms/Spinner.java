package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.SolenoidController;

public class Spinner extends Mechanism {

    private CANSpeedController m_talon;
    private SolenoidController m_spinner;
    private SolenoidController m_stopper;

    public Spinner() {
        m_talon = RobotProvider.instance.getTalonCANMotor("spinner.talon");
        m_spinner = RobotProvider.instance.getSolenoid("spinner.actuator");
        m_stopper = RobotProvider.instance.getSolenoid("spinner.stopper");
    }

    public void setPower(double spinnerPower) {
        m_talon.set(spinnerPower);
    }

    public void extendSpinner(){
        m_spinner.set(true);
    }

    public void retractSpinner() {
        m_spinner.set(false);
    }

    public void extendStopper() {
        m_stopper.set(true);
    }

    public void retractStopper() {
        m_stopper.set(false);
    }
}