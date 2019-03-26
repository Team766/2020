package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.GyroReader;
import com.team766.hal.CANSpeedController;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;

import com.team766.controllers.PIDController;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team766.config.ConfigFileReader;



public class Drive extends Mechanism  implements DriveI {

    private CANSpeedController m_leftVictor1;
    private CANSpeedController m_leftVictor2;
    private CANSpeedController m_rightVictor1;
    private CANSpeedController m_rightVictor2;
    private CANSpeedController m_leftTalon;
    private CANSpeedController m_rightTalon;
    private GyroReader m_gyro;
    public final double P = 0.04;
    public final double I = 0.0;
    public final double D = 0.004;
    public final double MP = 1.0;
    public final double MI = 0.0;
    public final double MD = 0.0;
    public static final double THRESHOLD = 2;
    public final double MIN_TURN_SPEED = 0.1;
    public final double DIST_PER_PULSE = ConfigFileReader.getInstance().getDouble("drive.DIST_PER_PULSE").get();
    public final double robotWidth = 2.8;
    public boolean m_secondVictor = true;
    public double m_gyroDirection = 1.0;

    public final double hatchHeight = 0;
    public final double mountingHeight = 0;
    public final double mountingAngle = 0;

    public final double velocityFactor = 1260.0;

    public Drive() { 
        m_leftVictor1 = RobotProvider.instance.getVictorCANMotor("drive.leftVictor1"); 
        m_rightVictor1 = RobotProvider.instance.getVictorCANMotor("drive.rightVictor1");
        if (ConfigFileReader.getInstance().getInt("drive.leftVictor2").get() >= 0) {
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
        // left true right false for new, both false for mule and marie
        m_leftTalon.setSensorPhase(true);
        m_rightTalon.setSensorPhase(false);
        m_leftTalon.configNominalOutputForward(0);
        m_leftTalon.configNominalOutputReverse(0);
        m_leftTalon.configPeakOutputForward(1);
        m_leftTalon.configPeakOutputReverse(-1);
        m_rightTalon.configNominalOutputForward(0);
        m_rightTalon.configNominalOutputReverse(0);
        m_rightTalon.configPeakOutputForward(1);
        m_rightTalon.configPeakOutputReverse(-1);
        m_leftTalon.config_kP(0, MP, 0);
        m_leftTalon.config_kI(0, MI, 0);
        m_leftTalon.config_kD(0, MD, 0);
        m_rightTalon.config_kP(0, MP, 0);
        m_rightTalon.config_kI(0, MI, 0);
        m_rightTalon.config_kD(0, MD, 0);
        m_leftTalon.setNeutralMode(NeutralMode.Coast);
        m_rightTalon.setNeutralMode(NeutralMode.Coast);
        m_leftTalon.configOpenLoopRamp(0.5, 0);
        m_leftTalon.configClosedLoopRamp(0.5, 0);
        m_rightTalon.configOpenLoopRamp(0.5, 0);
        m_rightTalon.configClosedLoopRamp(0.5, 0);
        m_gyroDirection = ConfigFileReader.getInstance().getDouble("drive.gyroDirection").get();
    }

    @Override
    public double getDistPerPulse() {
        return DIST_PER_PULSE;
    }

    /**
    * Sets the mode and value for the left and right Talon controllers.
    * Each Talon is followed by 2 Victors, which mirror the Talon's output.
    */
    public void setDrive(double leftSetting, double rightSetting) {
        m_leftTalon.set(ControlMode.Velocity, leftSetting * velocityFactor);
        m_rightTalon.set(ControlMode.Velocity, rightSetting * velocityFactor);
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

    public boolean isAutonomous() {
        return(DriverStation.getInstance().isAutonomous());
    }

    public double getGyroAngle() {
        return((m_gyro.getAngle() % 360) * m_gyroDirection);
    }

    public void resetGyro() {
        m_gyro.reset(); 
    }

    public void resetEncoders() {
        m_leftTalon.setPosition(0);
        m_rightTalon.setPosition(0);
    }

    public double leftEncoderDistance() {
        return(m_leftTalon.getSensorPosition());
    }

    public double rightEncoderDistance() {
        return(m_rightTalon.getSensorPosition());
    }

    public double leftMotorVelocity() {
        return(m_leftTalon.getSensorVelocity());
    }

    public double rightMotorVelocity() {
        return(m_rightTalon.getSensorVelocity());
    }

    /**
    * Returns the object of the specified encoder.
    * turnDirection = true returns the left encoder, and false returns the right encoder.
    */
    public double getOutsideEncoderDistance(boolean turnDirection) {
        if (turnDirection) {
            return(leftEncoderDistance());
        } else {
            return(rightEncoderDistance());
        }
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

    public void nukeRobot() {
        shutdown();
        resetEncoders();
        resetGyro();
    }
}
