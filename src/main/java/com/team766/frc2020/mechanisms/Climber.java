package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.SolenoidController;

public class Climber extends Mechanism {

    // Climber is pneumatics so I'm not sure how that's going to work
    private final SolenoidController m_climbPiston;
    private final CANSpeedController m_winch;
    private final CANSpeedController m_shifter;

    public Climber() {
        m_climbPiston = RobotProvider.instance.getSolenoid("climber.piston");
        m_winch = RobotProvider.instance.getTalonCANMotor("climber.winch");
        m_shifter = RobotProvider.instance.getTalonCANMotor("climber.shifter");
    }

    public void extend() {
        m_climbPiston.set(true);
    }

    public void retract() {
        m_climbPiston.set(false);
    }

    public void winchPower(final double speed){
        m_winch.set(speed);
    }

    public void shifterPower(final double speed){
        m_shifter.set(speed);
    }
}

