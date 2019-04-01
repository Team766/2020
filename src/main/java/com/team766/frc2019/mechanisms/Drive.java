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
    public static double P = 0.04;
    public static double I = 0.0005;
    public static double D = 0.0012;
    public final double MF = 1.1366666666666666666666666;
    public final double MP = 0.00; //0.02
    public final double MI = 0.00;
    public final double MD = 0.00;
    public static final double THRESHOLD = 2;
    public final double MIN_TURN_SPEED = 0.3;
    public final double DIST_PER_PULSE = ConfigFileReader.getInstance().getDouble("drive.DIST_PER_PULSE").get();
    public final double robotWidth = 2.8;
    public boolean m_secondVictor = true;
    public double m_gyroDirection = 1.0;

    public double leftSensorBasePosition;
    public double rightSensorBasePosition;

    public final double maximumRPM = 15 * 12 * 60 / 6.25; //first is feet/second, converts to RPM


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
        
        m_gyro = RobotProvider.instance.getGyro("drive.gyro");
        m_gyroDirection = ConfigFileReader.getInstance().getDouble("drive.gyroDirection").get();
        
        m_leftTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_rightTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_rightTalon.setInverted(true);
        m_rightVictor1.setInverted(true);
        if (m_secondVictor) {
            m_rightVictor2.setInverted(true);
        }
        // left true right false for new, both false for mule and marie
        m_leftTalon.setSensorPhase(false);
        m_rightTalon.setSensorPhase(false);
        m_leftTalon.config_kF(0, MF, 0);
        m_leftTalon.config_kP(0, MP, 0);
        m_leftTalon.config_kI(0, MI, 0);
        m_leftTalon.config_kD(0, MD, 0);
        m_rightTalon.config_kF(0, MF, 0);
        m_rightTalon.config_kP(0, MP, 0);
        m_rightTalon.config_kI(0, MI, 0);
        m_rightTalon.config_kD(0, MD, 0);
        m_leftTalon.setNeutralMode(NeutralMode.Coast);
        m_rightTalon.setNeutralMode(NeutralMode.Coast);
        /*m_leftTalon.configOpenLoopRamp(0.5, 0);
        m_leftTalon.configClosedLoopRamp(0.5, 0);
        m_rightTalon.configOpenLoopRamp(0.5, 0);
        m_rightTalon.configClosedLoopRamp(0.5, 0); IF SHIT BREAKS FOR THE LOVE OF GOD UNCOMMENT THIS*/
    }

    @Override
    public double getDistPerPulse() {
        return DIST_PER_PULSE;
    }

    /**
    * Sets the mode and value for the left and right Talon controllers.
    * Each Talon is followed by 2 Victors, which mirror the Talon's output.
    * Speed will be [-maximumRPM, maximumRPM], depending on joystick input.
    */
    public void setDrive(double leftSetting, double rightSetting) {
        m_leftTalon.set(ControlMode.Velocity, leftSetting * maximumRPM * 256 / 600); //RPM times units per rev / 100ms per min
        m_rightTalon.set(ControlMode.Velocity, rightSetting * maximumRPM * 256 / 600); //basically converts from RPM to units/100ms for the PID to use
        m_leftVictor1.follow(m_leftTalon);
        m_rightVictor1.follow(m_rightTalon);
        if (m_secondVictor) {
            m_leftVictor2.follow(m_leftTalon);
            m_rightVictor2.follow(m_rightTalon);
        }
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

    //makes encoders act like relative encoders
    public void resetEncoders() {
        leftSensorBasePosition = m_leftTalon.getSensorPosition();
        rightSensorBasePosition = m_rightTalon.getSensorPosition();
    }

    public double leftEncoderDistance() {
        return(m_leftTalon.getSensorPosition() - leftSensorBasePosition);
    }

    public double rightEncoderDistance() {
        return(m_rightTalon.getSensorPosition() - rightSensorBasePosition);
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

    //you die now
    public void nukeRobot() {
        shutdown();
        resetEncoders();
        resetGyro();
    }
}
