package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.GyroReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.SpeedController;
import com.team766.controllers.PIDController;


public class Drive extends Mechanism { 

    private SpeedController m_leftMotor;
    private SpeedController m_rightMotor;
    private GyroReader m_gyro;
    private PIDController m_turnController;
    private static double P = 10;
    private static double I = 2;
    private static double D = 20;
    private static double THRESHOLD = 0.2;

    public Drive() { 
        m_leftMotor = RobotProvider.instance.getMotor("drive.leftMotor");
        m_rightMotor = RobotProvider.instance.getMotor("drive.rightMotor");
        m_rightMotor.setInverted(true);
        m_gyro = RobotProvider.instance.getGyro("drive.gyro");
    }

    public void setDrivePower(double leftPower, double rightPower) {
        m_leftMotor.set(leftPower);
        m_rightMotor.set(rightPower);
    }

    public double getGyroAngle() {
        return(m_gyro.getAngle());
    }

    public void resetGyro() {
        m_gyro.reset(); 
    }

    /*@Override
    public void run() {
        setDrivePower(leftPower, rightPower);
    }
    */

    public void startTurn(double angle) {
        resetGyro(); 
        m_turnController = new PIDController(P, I, D, THRESHOLD);
        m_turnController.setSetpoint(angle);
        m_turnController.setMaxoutputHigh(0.5);      
        m_turnController.setMaxoutputLow(0.1);
    }
        
    public boolean isTurnDone() {
        if (m_turnController == null) {
            return true;
        }
        return m_turnController.isDone();
    }

    public void run() {
        if (m_turnController != null) {
            double currentAngle = m_gyro.getAngle();
            m_turnController.calculate(currentAngle, true);
            double power = m_turnController.getOutput();
            setDrivePower(power, -power);
            System.out.println("current angle is " + currentAngle + " power is " + power);

            if (m_turnController.isDone()) {
                m_turnController = null;
            }
        }
    }
}