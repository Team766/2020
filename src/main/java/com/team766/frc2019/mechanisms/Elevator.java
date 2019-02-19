package com.team766.frc2019.mechanisms;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team766.framework.Mechanism;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;


public class Elevator extends Mechanism {

    private CANSpeedController m_lowerElevatorMotor;
    private CANSpeedController m_upperElevatorMotor;
    public static double DIST_PER_PULSE = Robot.drive.DIST_PER_PULSE;
    public static double targetPosition;

    private static int MIN_LOWER_HEIGHT = 0;
	private static int NEAR_MIN_LOWER_HEIGHT = 100000;
    private static int NEAR_MAX_LOWER_HEIGHT = 2030000;
    private static int MAX_LOWER_HEIGHT = 2130000;
	private static int MIN_UPPER_HEIGHT = 0;
    private static int NEAR_MIN_UPPER_HEIGHT = 50000;
    private static int NEAR_MAX_UPPER_HEIGHT = 910000;
    private static int MAX_UPPER_HEIGHT = 920000;
	private static int MID_HEIGHT_BIG = 1000000;
	private static int MAX_HEIGHT_BIG = 1930000;
	private static int MID_HEIGHT_SMALL = 500000;
    private static int MAX_HEIGHT_SMALL = 900000;
    public static int LVL1 = 75000;
    public static int LVL1N = 50000;
    public static int LVL2 = 1500000;
    public static int LVL3 = 2870000;
    public static boolean combinedStopTargeting = false;
    public static boolean upperStopTargeting = false;


    public Elevator() {
        m_lowerElevatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.elevatorMotor");
        m_upperElevatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.actuatorMotor");
        m_lowerElevatorMotor.configFactoryDefault();
        m_upperElevatorMotor.configFactoryDefault();
        m_upperElevatorMotor.setInverted(true);
        m_lowerElevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_upperElevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_lowerElevatorMotor.setSensorPhase(true);
        m_upperElevatorMotor.setSensorPhase(false);
        m_lowerElevatorMotor.configNominalOutputForward(0.0);
        m_lowerElevatorMotor.configNominalOutputReverse(0.0);
        m_lowerElevatorMotor.configPeakOutputForward(1.0);
        m_lowerElevatorMotor.configPeakOutputReverse(-1.0);
        m_upperElevatorMotor.configNominalOutputForward(0.0);
        m_upperElevatorMotor.configNominalOutputReverse(0.0);
        m_upperElevatorMotor.configPeakOutputForward(1.0);
        m_upperElevatorMotor.configPeakOutputReverse(-1.0);
        m_lowerElevatorMotor.config_kP(0, 0.05, 0);
        m_lowerElevatorMotor.config_kI(0, 0.0, 0);
        m_lowerElevatorMotor.config_kD(0, 0.01, 0);
        m_lowerElevatorMotor.config_kF(0, 0.0, 0);
        m_upperElevatorMotor.config_kP(0, 0.05, 0);
        m_upperElevatorMotor.config_kI(0, 0.0, 0);
        m_upperElevatorMotor.config_kD(0, 0.01, 0);
        m_upperElevatorMotor.config_kF(0, 0.0, 0);
        m_lowerElevatorMotor.setNeutralMode(NeutralMode.Brake);
        m_upperElevatorMotor.setNeutralMode(NeutralMode.Brake);
        m_upperElevatorMotor.setPosition(0);
        m_lowerElevatorMotor.setPosition(0);
    }

    public void setLowerPower(double elevatorPower) {
        m_lowerElevatorMotor.set(ControlMode.PercentOutput, elevatorPower);
    }

    public void setUpperPower(double actuatorPower) {
        m_upperElevatorMotor.set(ControlMode.PercentOutput, actuatorPower);
    }

    public void setLowerPosition(double position) {
        m_lowerElevatorMotor.set(ControlMode.Position, position);
    }
    
    public void setUpperPosition(double position) {
        System.out.println("Setting upper position to: " + position);
        m_upperElevatorMotor.set(ControlMode.Position, position);
    }
    
    public void setCombinedPosition(double position) {
        System.out.println("Setting position to: " + position + " UH: " + getUpperHeight() + " LH: " + getLowerHeight());
        if (position > 0 && position < (double)(MAX_LOWER_HEIGHT + MAX_UPPER_HEIGHT) && combinedStopTargeting == false) {
            setLowerPosition((213*position)/308);
            setUpperPosition((95*position/308));
            targetPosition = position;
            System.out.println("TARGETPOSITION: " + targetPosition);
        } else {
            System.out.println("Cannot reach target position");
        }
    }

    public void addToPosition(double add) {
        }
    }

    public void setUpperHeight(double position, double power) {
        while (getUpperHeight() != position) {
            if (getUpperHeight() > position) {
                setUpperPower(-power);
            } else {
                setUpperPower(power);
            }
        }
    }

    public void elevatorUp() {
        combinedStopTargeting = true;
        System.out.println("LH: " + Robot.elevator.getLowerHeight() + " UH: " + Robot.elevator.getUpperHeight());
        if (Robot.elevator.getLowerHeight() > NEAR_MAX_LOWER_HEIGHT) {
            if (Robot.elevator.getLowerHeight() >= MAX_LOWER_HEIGHT) {
                Robot.elevator.setLowerPower(0.0);
            } else {
                Robot.elevator.setLowerPower(0.2);
            }
            if (Robot.elevator.getUpperHeight() > MAX_UPPER_HEIGHT) {
                Robot.elevator.setUpperPower(0.0);
            } else if (Robot.elevator.getUpperHeight() > NEAR_MAX_UPPER_HEIGHT) {
                System.out.println("UPPER NEARING DESTINATION");
                Robot.elevator.setUpperPower(0.3);
            } else {
                Robot.elevator.setUpperPower(0.9);
            }
        } else {
            Robot.elevator.setLowerPower(0.9);
        }
    }

    public void elevatorDown() {
        combinedStopTargeting = true;
        System.out.println("LH: " + Robot.elevator.getLowerHeight() + " UH: " + Robot.elevator.getUpperHeight());
        if (Robot.elevator.getUpperHeight() < NEAR_MIN_UPPER_HEIGHT) {
            if (Robot.elevator.getLowerHeight() < MIN_LOWER_HEIGHT) {
                Robot.elevator.setLowerPower(0.0);
            } else if (Robot.elevator.getLowerHeight() < NEAR_MIN_LOWER_HEIGHT) {
                Robot.elevator.setLowerPower(-0.1);
            }  else {
                Robot.elevator.setLowerPower(-0.75);
            }
            if (Robot.elevator.getUpperHeight() <= MIN_UPPER_HEIGHT) {
                Robot.elevator.setUpperPower(0.0);
            } else {
                Robot.elevator.setUpperPower(-0.2);
            }
        } else {
            Robot.elevator.setUpperPower(-0.75);
        }
    }

    public void elevatorNeutral() {
        if (combinedStopTargeting == true) {
            System.out.println("RUNNING elevatorNeutral");
            Robot.elevator.setUpperPower(0.0);
            Robot.elevator.setLowerPower(0.0);
            targetPosition = getLowerHeight() + getUpperHeight();
            combinedStopTargeting = false;
        }
    }

    public void upperNeutral() {
        if (upperStopTargeting == true) {
            System.out.println("RUNNING upperNeutral");
            Robot.elevator.setUpperPower(0.0);
            targetPosition = getUpperHeight();
            upperStopTargeting = false;
        }
    }


    /**
    * Move the elevator motor a set number of inches.
    **/
    public void moveLowerDistance(double distance) {
        m_lowerElevatorMotor.set(ControlMode.Position, distance / DIST_PER_PULSE);
    }

    /**
    * Move the gripper motor a set number of inches.
    **/
    public void moveUpperDistance(double distance) {
        m_upperElevatorMotor.set(ControlMode.Position, distance / DIST_PER_PULSE);
    }
}

