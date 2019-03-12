package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
//import com.team766.hal.RobotProvider;

public class LimeDebug extends Subroutine {
    public LimeDebug() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        while (Robot.drive.isEnabled()) {
            if (Robot.m_oi.driverControl) {
                System.out.println("ok so basically im stopping");
            } else { 
                System.out.println("im currently running an auto program");
            }
        }
        Robot.drive.nukeRobot();
        yield();
    }
}
