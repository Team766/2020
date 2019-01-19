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
    private static double P = 0.025;
    private static double I = 0.025;
    private static double D = 0.0;
    private static double THRESHOLD = 0.5;
    private static double MAX_TURN_SPEED = 0.75;

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
        m_turnController.setMaxoutputHigh(MAX_TURN_SPEED);      
        m_turnController.setMaxoutputLow(-MAX_TURN_SPEED);
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

            System.out.println("current angle is " + currentAngle + " error? " + m_turnController.getCurrentError());
            if (m_turnController.isDone()) {
                setDrivePower(0, 0);
                m_turnController = null;
                return;
            } 

            double power = m_turnController.getOutput();
            setDrivePower(power, -power);
            System.out.println("current angle is " + currentAngle + " power is " + power);
        }
    }
}