package com.team766.frc2020.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
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
        m_turnController = new PIDController(0.001, 0.001, 0.001, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        // TODO: change these hard coded PID values when we start doing the tuning 
        // (note I hard coded because there was a problem with overshooting and the robot 
        // just thinking it's done and not taking into account inertia that makes it keep going after ending)
        // takeControl(Robot.drive);
    }
    
    protected void subroutine() {

        turning = true;
        double power = 0;
        System.out.println("hey im gonna turn");
        m_turnController.setSetpoint(0.0);
        m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_turnAngle), true);
        while((!(Robot.drive.isTurnDone(m_turnController)) && Math.abs(m_joystick1.getRawAxis(1)) < .2)) {
            m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_turnAngle), true);
            power = m_turnController.getOutput();

            Robot.drive.setDrive(-power, power);

            SmartDashboard.putNumber("Current Angle", Robot.drive.getGyroAngle());
            SmartDashboard.putNumber("Target Angle", m_turnAngle);
            SmartDashboard.putNumber("Angle Difference", Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_turnAngle));
            SmartDashboard.putNumber("PID Output", m_turnController.getOutput());

            if (!Robot.drive.isEnabled()){
                turning = false;
                Robot.drive.nukeRobot();
                yield();
                return;
            }
        }

        Robot.drive.setDrive(0.0, 0.0);
        // Robot.drive.resetEncoders();
        yield();
        turning = false;
        System.out.println("exited loop");
        return;
    }
}
