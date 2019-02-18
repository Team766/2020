package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.GyroReader;
import com.team766.hal.CANSpeedController;
import com.team766.hal.EncoderReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;

import com.team766.controllers.PIDController;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.sun.security.auth.login.ConfigFile;
import com.team766.config.ConfigFileReader;



public class Drive extends Mechanism { 

    private CANSpeedController m_leftVictor1;
    private CANSpeedController m_leftVictor2;
    private CANSpeedController m_rightVictor1;
    private CANSpeedController m_rightVictor2;
    private CANSpeedController m_leftTalon;
    private CANSpeedController m_rightTalon;
    private GyroReader m_gyro;
    //private EncoderReader m_leftEncoder; 
    //private EncoderReader m_rightEncoder;
    public static double P = 0.04;
    public static double I = 0.01;
    public static double D = 0.004;
    public static double THRESHOLD = 3;
    public static double MAX_TURN_SPEED = 0.75;
    public static double MIN_TURN_SPEED = 0.1;
    public static double DIST_PER_PULSE = ConfigFileReader.getInstance().getDouble("drive.DIST_PER_PULSE").get(); //0.00159616132
    public static double POSITION_PER_INCH = 20000;
    public static double robotWidth = 2.8;
    public static boolean m_secondVictor = true;
    public double m_gyroDirection = 1.0;
    

    public Drive() { 
        m_leftVictor1 = RobotProvider.instance.getVictorCANMotor("drive.leftVictor1"); 
        m_rightVictor1 = RobotProvider.instance.getVictorCANMotor("drive.rightVictor1");
        if ( ConfigFileReader.getInstance().getInt("drive.leftVictor2").get() >= 0) {
            m_secondVictor = true;
            m_leftVictor2 = RobotProvider.instance.getVictorCANMotor("drive.leftVictor2");
            m_rightVictor2 = RobotProvider.instance.getVictorCANMotor("drive.rightVictor2");
        } else {
            m_secondVictor = false;
        }
        m_leftTalon = RobotProvider.instance.getTalonCANMotor("drive.leftTalon");
        m_rightTalon = RobotProvider.instance.getTalonCANMotor("drive.rightTalon");
        
        //m_leftEncoder = RobotProvider.instance.getEncoder("drive.leftEncoder");
        //m_rightEncoder = RobotProvider.instance.getEncoder("drive.rightEncoder");
        m_gyro = RobotProvider.instance.getGyro("drive.gyro");
        m_leftTalon.configFactoryDefault();
        m_rightTalon.configFactoryDefault();
        m_leftTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_rightTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_rightTalon.setInverted(true);
        m_rightVictor1.setInverted(true);
        if (m_secondVictor) {
            m_rightVictor2.setInverted(true);
        }
        // left false right true for new, both false for mule
        m_leftTalon.setSensorPhase(false);
        m_rightTalon.setSensorPhase(false);
        m_leftTalon.configNominalOutputForward(0);
        m_leftTalon.configNominalOutputReverse(0);
        m_leftTalon.configPeakOutputForward(1);
        m_leftTalon.configPeakOutputReverse(-1);
        m_rightTalon.configNominalOutputForward(0);
        m_rightTalon.configNominalOutputReverse(0);
        m_rightTalon.configPeakOutputForward(1);
        m_rightTalon.configPeakOutputReverse(-1);
        m_leftTalon.config_kP(0, 0.05, 0);
        m_leftTalon.config_kI(0, 0.01, 0);
        m_leftTalon.config_kD(0, 0.01, 0);
        m_leftTalon.config_kF(0, 0.0, 0);
        m_rightTalon.config_kP(0, 0.05, 0);
        m_rightTalon.config_kI(0, 0.01, 0);
        m_rightTalon.config_kD(0, 0.01, 0);
        m_rightTalon.config_kF(0, 0.0, 0);
        m_leftTalon.setNeutralMode(NeutralMode.Brake);
        m_rightTalon.setNeutralMode(NeutralMode.Brake);
        m_leftTalon.configOpenLoopRamp(0.5, 0);
        m_leftTalon.configClosedLoopRamp(0.5, 0);
        m_rightTalon.configOpenLoopRamp(0.5, 0);
        m_rightTalon.configClosedLoopRamp(0.5, 0);
        //encodersDistancePerPulse(DIST_PER_PULSE);
        m_gyroDirection = ConfigFileReader.getInstance().getDouble("drive.gyroDirection").get();
    }

    /**
    * Sets the mode and value for the left and right Talon controllers.
    * Each Talon is followed by 2 Victors, which mirror the Talon's output.
    */
    public void setDrive(double leftSetting, double rightSetting, ControlMode controlMode) {
        m_leftTalon.set(controlMode, leftSetting);
        m_rightTalon.set(controlMode, rightSetting);
        m_leftVictor1.follow(m_leftTalon);
        m_rightVictor1.follow(m_rightTalon);
        if (m_secondVictor == true) {
            m_leftVictor2.follow(m_leftTalon);
            m_rightVictor2.follow(m_rightTalon);
        }
        /*m_leftVictor1.setNeutralMode(NeutralMode.Coast);
        m_leftVictor2.setNeutralMode(NeutralMode.Coast);
        m_rightVictor1.setNeutralMode(NeutralMode.Coast);
        m_rightVictor2.setNeutralMode(NeutralMode.Coast);*/
    }

    public boolean isEnabled() {
        return(DriverStation.getInstance().isEnabled());
    }

    public double getGyroAngle() {
        return(m_gyro.getAngle() * m_gyroDirection);
    }

    public void resetGyro() {
        m_gyro.reset(); 
    }

    public double leftEncoderDistance() {
        return(m_leftTalon.getSensorPosition());
    }

    public double rightEncoderDistance() {
        return(m_rightTalon.getSensorPosition());
    }

    /**
    * Returns the object of the specified encoder.
    * turnDirection = true returns the right encoder, and false returns the left encoder.
    */
    public double getOutsideEncoderDistance(boolean turnDirection) {
        if (turnDirection) {
            return(leftEncoderDistance());
        } else {
            return(rightEncoderDistance());
        }
    }

    public void resetEncoders() {
        m_leftTalon.setPosition(0);
        m_rightTalon.setPosition(0);
        //m_leftEncoder.reset();
        //m_rightEncoder.reset();
    }

    /*public void encodersDistancePerPulse(double distancePerPulse) {
        m_leftEncoder.setDistancePerPulse(distancePerPulse);
        m_rightEncoder.setDistancePerPulse(distancePerPulse);
    }*/

    public void shutdown() {
        m_leftTalon.set(ControlMode.PercentOutput, 0);
        m_rightTalon.set(ControlMode.PercentOutput, 0);
        m_leftTalon.setNeutralMode(NeutralMode.Coast);
        m_rightTalon.setNeutralMode(NeutralMode.Coast);
    }
    
    public boolean isTurnDone(PIDController turnController) {
        if (turnController == null) {
            return true;
        }
        return turnController.isDone();
    }

    /**
	 * Gets the difference between angle1 and angle2, between -180 and 180 degrees.
     * angle1 is the current angle, angle2 is the desired angle.
	 */
    public double AngleDifference(double angle1, double angle2) { //356, 0
        double diff = (angle2 - angle1 + 180) % 360 - 180;
        return diff < -180 ? diff + 360 : diff;
        //return diff;
    }
}