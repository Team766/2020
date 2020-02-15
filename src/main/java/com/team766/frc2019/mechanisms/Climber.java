package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;


public class Climber extends Mechanism {

    private CANSpeedController m_climber;

    public Climber() {
        m_climber = RobotProvider.instance.getInsertMotorHereorwhateveritis("climber.motor");
    }

    public void extend() {
        // insert code to extend the motor
    }

    public void retract() {
        // insert code to retract motor
    }
}

