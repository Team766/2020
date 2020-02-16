package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.SolenoidController;

public class Climber extends Mechanism {

    // Climber is pneumatics so I'm not sure how that's going to work
    private SolenoidController m_climbPiston;
    private CANSpeedController m_winch;

    public Climber() {
        m_climbPiston = RobotProvider.instance.getSolenoid("climber.piston");
        m_winch = RobotProvider.instance.getTalonCANMotor("climber.winch");
    }

    public void extend() {
        m_climbPiston.set(true);
    }

    public void retract() {
        m_climbPiston.set(false);
    }

    public void winchSpeed(double speed){
        m_winch.set(speed);
    }
}

