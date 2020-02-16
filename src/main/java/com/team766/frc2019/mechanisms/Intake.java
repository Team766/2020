package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;


public class Intake extends Mechanism {

    private CANSpeedController m_victor;

    public Intake() {
        m_victor = RobotProvider.instance.getVictorCANMotor("intake.victor");
    }

    public void setPower(double intakePower) {
        m_victor.set(intakePower);
    }
}