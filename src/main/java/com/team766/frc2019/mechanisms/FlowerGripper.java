package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.SolenoidController;

public class FlowerGripper extends Mechanism {

    private SolenoidController m_gripper;

    public FlowerGripper(){
        m_gripper = RobotProvider.instance.getSolenoid("gripper.flower"); 
    }

    public void extend() {
        m_gripper.set(true);
    }

    public void retract() {
        m_gripper.set(false);
    }
}

