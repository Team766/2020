package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;

public class LimeDebug extends Subroutine {

    private JoystickReader m_joystick1;
    private JoystickReader m_joystick2;
    private JoystickReader m_boxop;

    private double minimumLevel = 0.05;

    public LimeDebug() {
		m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        while (Robot.drive.isEnabled()) {
            if (isAnythingEnabled()) {
                System.out.println("ok so basically im stopping");
            } else { 
                System.out.println("Is Target: " + Robot.limeLight.isTarget() + " X: " + Robot.limeLight.tx() + " Y: " + Robot.limeLight.ty() + " Area: " + Robot.limeLight.ta());
            }
        }
        Robot.drive.nukeRobot();
        yield();
    }

    private boolean isAnythingEnabled() {
        System.out.println(m_joystick1.getRawAxis(0));
        if (Math.abs(m_joystick1.getRawAxis(0)) > minimumLevel || 
                Math.abs(m_joystick1.getRawAxis(1)) > minimumLevel || 
                Math.abs(m_joystick2.getRawAxis(0)) > minimumLevel || 
                Math.abs(m_joystick2.getRawAxis(1)) > minimumLevel ||
                m_boxop.getRawButton(17)) {
            return true;
        } else {
            return false;
        }
    }
}
