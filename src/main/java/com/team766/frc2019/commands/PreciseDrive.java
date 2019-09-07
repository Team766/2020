package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import javax.swing.text.StyleContext.SmallAttributeSet;

import com.team766.controllers.PIDController;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;

public class PreciseDrive extends Subroutine {

    private JoystickReader m_joystick1  = RobotProvider.instance.getJoystick(1);
    PIDController m_turnController;
    double m_driveDistance;
    double m_targetAngle;
    double m_targetPower;
    double m_startPower;
    double m_endPower;
    double m_adjustment;
    double MIN_POWER = 0.25;
    double POWER_RAMP = 1.0;
    int driveDir = 1;
    double END_POWER_PERCENT = 0.75;

    /**
     * Precisely drives for the set parameters.
     * @param targetAngle
     * @param driveDistance
     * @param targetPower
     * @param endPower
     */
    public PreciseDrive(double targetAngle, double driveDistance, double targetPower, double endPower) {
        
        

    }

    protected void subroutine() {
        



    }

    public double getCurrentDistance() {
        return(((Robot.drive.rightEncoderDistance() + Robot.drive.leftEncoderDistance())*Robot.drive.DIST_PER_PULSE)/2.0);
    }

    private double calcPower(double arcPercent) {
        



    }

}