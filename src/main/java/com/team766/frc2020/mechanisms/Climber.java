package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.SolenoidController;

public class Climber extends Mechanism {

    private SolenoidController m_climbPistonUp;
    private SolenoidController m_climbPistonDown;
    private CANSpeedController m_winch;
    private CANSpeedController m_shifter;

    public Climber() {
        if (true) {
            m_climbPistonUp = RobotProvider.instance.getSolenoid("climber.pistonUp");
            m_climbPistonDown = RobotProvider.instance.getSolenoid("climber.pistonDown");
            m_winch = RobotProvider.instance.getVictorCANMotor("climber.winch");
            m_shifter = RobotProvider.instance.getVictorCANMotor("climber.shifter");
            setClimberUp(false);
        }
    }

    public void setClimberUpState(boolean state) {
        if (m_climbPistonUp != null) {
        m_climbPistonUp.set(state);
        }
    }

    public void setClimberDownState(boolean state) {
        m_climbPistonDown.set(state);
    }

    public void setClimberUp (boolean isUp) {
        m_climbPistonDown.set(!isUp);
        m_climbPistonUp.set(isUp);
    }

    public void setWinchPower(double speed){
        m_winch.set(speed);
    }

    public void setShifterPower(double speed){
        m_shifter.set(speed);
    }
}

