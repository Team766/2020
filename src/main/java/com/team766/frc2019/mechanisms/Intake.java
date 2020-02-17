package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.SolenoidController;


public class Intake extends Mechanism {

    private CANSpeedController m_victor;
    private SolenoidController m_intakeActuator;

    public void intake() {
        m_victor = RobotProvider.instance.getVictorCANMotor("intake.victor");
        m_intakeActuator = RobotProvider.instance.getSolenoid("intake.actuator");
    }

    public void setPower(double intakePower) {
        m_victor.set(intakePower);
    }

    public void actuate() {
        m_intakeActuator.set(true);
    }

    public void retract() {
        m_intakeActuator.set(false);
    }
}