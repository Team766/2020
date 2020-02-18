package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;


public class Wagon extends Mechanism {

    private CANSpeedController m_victor;

    public Wagon() {
        m_victor = RobotProvider.instance.getVictorCANMotor("wagon.victor");
    }

    public void setWagonPower(double wagonPower) {
        m_victor.set(wagonPower);
    }
}