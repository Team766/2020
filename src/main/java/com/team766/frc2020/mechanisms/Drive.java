package com.team766.frc2020.mechanisms;

import java.lang.Math.*;

import com.team766.framework.Mechanism;
import com.team766.hal.GyroReader;
import com.team766.hal.CANSpeedController;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.team766.controllers.PIDController;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team766.config.ConfigFileReader;

import com.team766.frc2020.Robot;

public class Drive extends Mechanism implements DriveI {

    //vars (including PID)
    private CANSpeedController m_leftVictor1;
    private CANSpeedController m_leftVictor2;
    private CANSpeedController m_rightVictor1;
    private CANSpeedController m_rightVictor2;
    private static CANSpeedController m_leftTalon;
    private static CANSpeedController m_rightTalon;
    private GyroReader m_gyro;
    public static double P = 0.01; //0.04
    public static double I = 0.0;//0.0005
    public static double D = 0.0; //0.0012
    public final double MF = 1.1366666666666666666666666;
    public final double MP = 0.00; //0.02
    public final double MI = 0.00;
    public final double MD = 9.31;
    public static final double THRESHOLD = 2;
    public final double MIN_TURN_SPEED = 0.35;
    public final double DIST_PER_PULSE = ConfigFileReader.getInstance().getDouble("drive.DIST_PER_PULSE").get();
    public final double robotWidth = 2.8;
    public boolean m_secondVictor = true;
    public double m_gyroDirection = 1.0;

    public double leftSensorBasePosition;
    public double rightSensorBasePosition;

    public static boolean isInverted = false;

    public final double maximumRPM = 15 * 12 * 60 / 6.25; //first is feet/second, converts to RPM


    public Drive() {

        // Initialize victors
        m_leftVictor1 = RobotProvider.instance.getVictorCANMotor("drive.leftVictor1"); 
        m_rightVictor1 = RobotProvider.instance.getVictorCANMotor("drive.rightVictor1");

        // Initialize second victors if they exist
        // if (ConfigFileReader.getInstance().getInt("drive.leftVictor2").get() >= 0) {
        //     m_secondVictor = true;
        //     m_leftVictor2 = RobotProvider.instance.getVictorCANMotor("drive.leftVictor2");
        //     m_rightVictor2 = RobotProvider.instance.getVictorCANMotor("drive.rightVictor2");    
        // } else {
            m_secondVictor = false;
        // }

        // Initializes talons
        m_leftTalon = RobotProvider.instance.getTalonCANMotor("drive.leftTalon");
        m_rightTalon = RobotProvider.instance.getTalonCANMotor("drive.rightTalon");
        
        // Initialize gyro
        m_gyro = RobotProvider.instance.getGyro("drive.gyro");
        m_gyroDirection = ConfigFileReader.getInstance().getDouble("drive.gyroDirection").get();
        
        // Configure the motors, Set inversion
        m_leftTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_rightTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_rightTalon.setInverted(true);
        m_rightVictor1.setInverted(true);
        if (m_secondVictor) {
            m_rightVictor2.setInverted(true);
        }

        // Left true right false for new robot, both false for mule and marie
        // Invert sensors
        m_leftTalon.setSensorPhase(false);
        m_rightTalon.setSensorPhase(false);

        // Configure pid
        m_leftTalon.config_kF(0, MF, 0);
        m_leftTalon.config_kP(0, MP, 0);
        m_leftTalon.config_kI(0, MI, 0);
        m_leftTalon.config_kD(0, MD, 0);
        m_rightTalon.config_kF(0, MF, 0);
        m_rightTalon.config_kP(0, MP, 0);
        m_rightTalon.config_kI(0, MI, 0);
        m_rightTalon.config_kD(0, MD, 0);

        // Set resting modes for robot
        m_leftTalon.setNeutralMode(NeutralMode.Brake);
        m_rightTalon.setNeutralMode(NeutralMode.Brake);
        m_leftTalon.configOpenLoopRamp(0.25, 0);
        m_leftTalon.configClosedLoopRamp(0.25, 0);
        m_rightTalon.configOpenLoopRamp(0.25, 0);
        m_rightTalon.configClosedLoopRamp(0.25, 0); //if something breaks that you can't figure out with acceleration (un)comment this
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
        SmartDashboard.putNumber("Left Motor Input", leftSetting * maximumRPM * 256 / 600);
        SmartDashboard.putNumber("Right Motor Input", rightSetting * maximumRPM * 256 / 600);
    }

    public void arcadeDrive(double fwdPower, double turnPower) {
        double maximum = Math.max(Math.abs(fwdPower), Math.abs(turnPower));
        double total = fwdPower + turnPower;
        double difference = fwdPower - turnPower;

        if (fwdPower >= 0) {
            if (turnPower >= 0) {
                setDrive(maximum, difference);
            } else {
                setDrive(total, maximum);
            }
        } else {
            if (turnPower >= 0) {
                setDrive(total, -maximum);
            } else {
                setDrive(-maximum, difference);
            }
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

    public static boolean getInverted() {
        return isInverted;
    }

    public static void setInvertStatus(boolean isItInverted) {
        isInverted = isItInverted;
    }

    public static void invertMotors(){ //makes motors go backwards
            m_rightTalon.setInverted(false);
            m_leftTalon.setInverted(true);
    }

    public static void resetMotorOrientation() { //makes motors go backwards
        m_rightTalon.setInverted(true);
        m_leftTalon.setInverted(false);
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

    // public class EncoderData {

    // }

    // public encoderData getEncoderData() {

    // }

    //you die now
    public void nukeRobot() {
        shutdown();
        resetEncoders();
        resetGyro();
    }

    // variables for calculating position using odometry
    // should be moved later
    private static double deltaXPosition = 0;
    private static double deltaYPosition = 0;
    private static double xPosition = 0;
    private static double yPosition = 0;
    private static double velocity = 0;
    // heading is in degrees
    private static double heading = 0; //aka angle

    private static double previousTime = RobotProvider.getTimeProvider().get();
    private static double currentTime = previousTime;

    private double currentGyroAngle = 0;
    private double currentLeftEncoderDistance = 0;
    private double currentRightEncoderDistance = 0;
    int index = 0;

    public double getXPosition() {
        return xPosition;
    }

    public double getYPosition() {
        return yPosition;
    }

    public double getAngle() {
        return heading;
    }

    public double getVelocity() {
        return velocity;
    }

    @Override
    public void run() {    
        if (index == 0) {
            resetEncoders();
            resetGyro();
            index = 1;
        }

        // get data
        currentTime = RobotProvider.getTimeProvider().get();
        currentGyroAngle = getGyroAngle();
        currentLeftEncoderDistance = leftEncoderDistance();
        currentRightEncoderDistance = rightEncoderDistance();
        Robot.drive.resetEncoders();

        // calculate position
        deltaXPosition = (currentLeftEncoderDistance + currentRightEncoderDistance) / 2  * .019372 * Math.sin(Math.toRadians(currentGyroAngle));
        deltaYPosition = (currentLeftEncoderDistance + currentRightEncoderDistance) / 2  * .019372 * Math.cos(Math.toRadians(currentGyroAngle));

        xPosition += deltaXPosition;
        yPosition += deltaYPosition;

        // calculate velocity
        velocity = Math.sqrt(Math.pow(deltaXPosition, 2) + Math.pow(deltaYPosition, 2)) / (currentTime - previousTime);
        
        if (index % 10 == 0) {
            SmartDashboard.putNumber("X position", xPosition);
            SmartDashboard.putNumber("Y position", yPosition);
            SmartDashboard.putNumber("Gyro angle", currentGyroAngle);
            SmartDashboard.putNumber("velocity", velocity);
            // System.out.println("position in drive.java ("+ xPosition + ", "+ yPosition);
            // System.out.println("gyro angle  " + currentGyroAngle);
            // System.out.println("left encoder: " + currentLeftEncoderDistance + " right encoder " + currentRightEncoderDistance);
        }
        index++;

        previousTime = currentTime;
    }
}
