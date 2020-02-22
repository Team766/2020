package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;

public class Outtake extends Mechanism {

    private CANSpeedController m_talon1;
    private CANSpeedController m_talon2;

    public Outtake() {
        m_talon1 = RobotProvider.instance.getTalonCANMotor("outtake.leftTalon");
        m_talon2 = RobotProvider.instance.getTalonCANMotor("outtake.rightTalon");
        m_talon1.setInverted(true);
    }

    public void setOuttakePower(double outtakePower) {
        m_talon1.set(outtakePower);
        m_talon2.set(outtakePower);
    }

    public void setOuttakePower(double outtakePowerLeft, double outtakePowerRight) {
        m_talon1.set(outtakePowerLeft);
        m_talon2.set(outtakePowerRight);
    }

    public void stopOuttake() {
        m_talon1.stopMotor();
        m_talon2.stopMotor();
    }

}