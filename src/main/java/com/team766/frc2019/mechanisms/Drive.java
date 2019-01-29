package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.GyroReader;
import com.team766.hal.EncoderReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.SpeedController;
import com.team766.controllers.PIDController;


public class Drive extends Mechanism { 

    private SpeedController m_leftMotor;
    private SpeedController m_rightMotor;
    private GyroReader m_gyro;
    private EncoderReader m_leftEncoder; 
    private EncoderReader m_rightEncoder;
    private PIDController m_turnController;
    public static double P = 0.04;
    public static double I = 0;
    public static double D = 0.004;
    public static double THRESHOLD = 3;
    public static double MAX_TURN_SPEED = 0.75;
    public static double MIN_TURN_SPEED = 0.1;
    public static double DIST_PER_PULSE = 0.04987;
    

    public Drive() { 
        m_leftMotor = RobotProvider.instance.getMotor("drive.leftMotor");
        m_rightMotor = RobotProvider.instance.getMotor("drive.rightMotor");
        m_rightMotor.setInverted(true);
        encodersDistancePerPulse(DIST_PER_PULSE);
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

    public double leftEncoderDistance() { 
        return(m_leftEncoder.getDistance());
    }

    public double rightEncoderDistance() { 
        return(m_rightEncoder.getDistance());
    }

    public void resetEncoders() {
        m_leftEncoder.reset();
        m_rightEncoder.reset();
    }

    public void encodersDistancePerPulse(double distancePerPulse) {
        m_leftEncoder.setDistancePerPulse(distancePerPulse);
        m_rightEncoder.setDistancePerPulse(distancePerPulse);
    }

    /*@Override
    public void run() {
        setDrivePower(leftPower, rightPower);
    }
    */
        
    public boolean isTurnDone(PIDController turnController) {
        if (turnController == null) {
            return true;
        }
        return turnController.isDone();
    }
}