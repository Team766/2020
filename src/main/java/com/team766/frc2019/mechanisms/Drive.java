package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.GyroReader;
import com.team766.hal.CANSpeedController;
import com.team766.hal.EncoderReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.SpeedController;
import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.controllers.PIDController;


public class Drive extends Mechanism { 

    private SpeedController m_leftVictor;
    private SpeedController m_rightVictor;
    private CANSpeedController m_leftTalon;
    private CANSpeedController m_rightTalon;
    private GyroReader m_gyro;
    private EncoderReader m_leftEncoder; 
    private EncoderReader m_rightEncoder;
    public static double P = 0.04;
    public static double I = 0.0;
    public static double D = 0.004;
    public static double THRESHOLD = 3;
    public static double MAX_TURN_SPEED = 0.75;
    public static double MIN_TURN_SPEED = 0.1;
    public static double DIST_PER_PULSE = 0.04987;
    public static double robotWidth = 2.8;
    

    public Drive() { 
        m_leftVictor = RobotProvider.instance.getMotor("drive.leftMotor");
        m_rightVictor = RobotProvider.instance.getMotor("drive.rightMotor");
        m_leftTalon = RobotProvider.instance.getCANMotor("drive.leftController");
        m_rightTalon = RobotProvider.instance.getCANMotor("drive.rightController");
        m_leftEncoder = RobotProvider.instance.getEncoder("drive.leftEncoder");
        m_rightEncoder = RobotProvider.instance.getEncoder("drive.rightEncoder");
        m_gyro = RobotProvider.instance.getGyro("drive.gyro");
        m_rightVictor.setInverted(true);
        encodersDistancePerPulse(DIST_PER_PULSE);
    }

    public void setDrivePower(double leftPower, double rightPower) {
        m_leftVictor.set(leftPower);
        m_rightVictor.set(rightPower);
    }

    public void setDriveVelocity(double leftVelocity, double rightVelocity) {
        m_leftTalon.set(ControlMode.Velocity, leftVelocity);
        m_rightTalon.set(ControlMode.Velocity, rightVelocity);
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

    public EncoderReader getOutsideEncoder(boolean turnDirection) {
        if (turnDirection) {
            return(m_leftEncoder);
        } else {
            return(m_rightEncoder);
        }
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

    public double AngleDifference(double angle1, double angle2) {
        double diff = (angle2 - angle1 + 180) % 360 - 180;
        return diff < -180 ? diff + 360 : diff;
    }
}