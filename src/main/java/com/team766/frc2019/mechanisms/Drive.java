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
    private static double THRESHOLD = 5;
    private static double MAX_TURN_SPEED = 0.75;
    private static double MIN_TURN_SPEED = 0.2;
    private PIDController m_driveController;
    private static double maxDriveSpeed = 0.6;
    private static double minDriveSpeed = 0.2;
    private EncoderReader m_leftEncoder;
    private EncoderReader m_rightEncoder;


    public Drive() { 
        m_leftMotor = RobotProvider.instance.getMotor("drive.leftMotor");
        m_rightMotor = RobotProvider.instance.getMotor("drive.rightMotor");
        m_rightMotor.setInverted(true);
        m_gyro = RobotProvider.instance.getGyro("drive.gyro");
        m_rightEncoder = RobotProvider.instance.getEncoder("drive.rightEncoder");
        m_leftEncoder = RobotProvider.instance.getEncoder("drive.leftEncoder");
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

            //System.out.println("current angle is " + currentAngle + " error? " + m_turnController.getCurrentError());
            if (m_turnController.isDone()) {
                setDrivePower(0, 0);
                m_turnController = null;
                return;
            } 

            double power = m_turnController.getOutput();

            if (Math.abs(power) < MIN_TURN_SPEED) {
                if (power < 0) {
                    power = -MIN_TURN_SPEED;
                } else {
                    power = MIN_TURN_SPEED;
                }
            }

            setDrivePower(power, -power);
            System.out.println("current angle is " + currentAngle + " power is " + power + " error is " + m_turnController.getCurrentError());
        }
    }

    public void resetEncoders() {
        m_leftEncoder.reset();
        m_rightEncoder.reset();
    }
    
    public double rightEncoderDistance() {
        return(m_rightEncoder.getDistance());
    }
 

    public void startDriveStraight(double distance) {
        resetEncoders();
        m_driveController = new PIDController(P, I, D, THRESHOLD);
        m_driveController.setSetpoint(distance);
        m_driveController.setMaxoutputHigh(maxDriveSpeed);
        m_driveController.setMaxoutputLow(minDriveSpeed);
    }

    public boolean isDriveDone() {
        if (m_driveController == null) {
            return true;
        }
        return m_driveController.isDone();
    }

    public void runDrive() {
        if (m_driveController != null) {
            double currentDist = rightEncoderDistance();
            m_driveController.calculate(currentDist, true);
            
            if (m_driveController.isDone()) {
                setDrivePower(0,0);
                m_driveController = null;
                return;
            }
            
            double power = m_driveController.getOutput();
            
           if (Math.abs(power) < minDriveSpeed) {
               if (power < 0) {
                   power = -minDriveSpeed;
                } else {
                    power = minDriveSpeed;
                }
            }
            setDrivePower(-power, power);
            System.out.println("current distance is " + currentDist + " power is" + power);
        }
    }
}