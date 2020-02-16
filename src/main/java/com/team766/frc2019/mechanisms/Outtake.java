package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;


public class Outtake extends Mechanism {

    private CANSpeedController m_talon1;
    private CANSpeedController m_talon2;

    public Outtake() {
        m_talon1 = RobotProvider.instance.getTalonCANMotor("outtake.talon1");
        m_talon2 = RobotProvider.instance.getTalonCANMotor("outtake.talon2");
    }

    public void setPower(double outtakePower) {
        m_talon1.set(outtakePower);
        m_talon2.set(outtakePower);
    }
}