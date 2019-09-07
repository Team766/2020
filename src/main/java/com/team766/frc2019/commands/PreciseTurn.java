package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.team766.controllers.PIDController;
import com.team766.hal.CANSpeedController;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;

public class PreciseTurn extends Subroutine {

    //instantiates vars
    double m_turnAngle;
    PIDController m_turnController;
    private JoystickReader m_joystick1 = RobotProvider.instance.getJoystick(1);
    public static boolean turning = false; 

    //default constructor that sets values for the instance of the subiroutine
    public PreciseTurn(double turnAngle) {
        m_turnAngle = turnAngle;
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        //takeControl(Robot.drive);
    }
    
    protected void subroutine() {


    }
}
