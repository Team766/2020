package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;


public class Wagon extends Mechanism {

    private CANSpeedController m_frontVictor;
    private CANSpeedController m_backVictor;

    public Wagon() {
        m_frontVictor = RobotProvider.instance.getVictorCANMotor("wagon.frontVictor");
        m_backVictor = RobotProvider.instance.getVictorCANMotor("wagon.backVictor");
        m_frontVictor.setInverted(true);
        m_backVictor.setInverted(true);
    }

    public void setWagonPower(double wagonPower) {
        m_frontVictor.set(wagonPower);
        m_backVictor.set(wagonPower);
    }
}