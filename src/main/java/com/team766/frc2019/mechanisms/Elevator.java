package com.team766.frc2019.mechanisms;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team766.framework.Mechanism;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;


public class Elevator extends Mechanism {

    private CANSpeedController m_lowerStageMotor;
    private CANSpeedController m_upperStageMotor;
    public static double DIST_PER_PULSE = Robot.drive.DIST_PER_PULSE;
    public static double targetPosition;

    private static int MIN_HEIGHT = 0;
	private static int NEAR_MIN_HEIGHT = 75000;
	private static int MAX_HEIGHT = 2130000;
    private static int NEAR_MAX_HEIGHT = 2030000;
    private static int MAX_UPPER_STAGE_HEIGHT = 950000;
	private static int NEAR_MAX_UPPER_STAGE_HEIG = 930000;
	private static int MIN_HEIGHT_ACTUATOR = 0;
	private static int NEAR_MIN_ACTUATOR_HEIGHT = 50000;
	private static int MID_HEIGHT_BIG = 1000000;
	private static int MAX_HEIGHT_BIG = 1930000;
	private static int MID_HEIGHT_SMALL = 500000;
    private static int MAX_HEIGHT_SMALL = 900000;
    public static int LVL1 = 100;
    public static int LVL2 = 500000;
    public static int LVL3 = 1000000;


    public Elevator() {
        m_lowerStageMotor = RobotProvider.instance.getTalonCANMotor("elevator.elevatorMotor");
        m_upperStageMotor = RobotProvider.instance.getTalonCANMotor("elevator.actuatorMotor");
        m_lowerStageMotor.configFactoryDefault();
        m_upperStageMotor.configFactoryDefault();
        m_upperStageMotor.setInverted(true);
        m_lowerStageMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_upperStageMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_lowerStageMotor.setSensorPhase(true);
        m_upperStageMotor.setSensorPhase(false);
        m_lowerStageMotor.configNominalOutputForward(0.0);
        m_lowerStageMotor.configNominalOutputReverse(0.0);
        m_lowerStageMotor.configPeakOutputForward(1.0);
        m_lowerStageMotor.configPeakOutputReverse(-1.0);
        m_upperStageMotor.configNominalOutputForward(0.0);
        m_upperStageMotor.configNominalOutputReverse(0.0);
        m_upperStageMotor.configPeakOutputForward(1.0);
        m_upperStageMotor.configPeakOutputReverse(-1.0);
        m_lowerStageMotor.config_kP(0, 0.05, 0);
        m_lowerStageMotor.config_kI(0, 0.0, 0);
        m_lowerStageMotor.config_kD(0, 0.01, 0);
        m_lowerStageMotor.config_kF(0, 0.0, 0);
        m_upperStageMotor.config_kP(0, 0.05, 0);
        m_upperStageMotor.config_kI(0, 0.0, 0);
        m_upperStageMotor.config_kD(0, 0.01, 0);
        m_upperStageMotor.config_kF(0, 0.0, 0);
        m_lowerStageMotor.setNeutralMode(NeutralMode.Brake);
        m_upperStageMotor.setNeutralMode(NeutralMode.Brake);
        m_upperStageMotor.setPosition(0);
        m_lowerStageMotor.setPosition(0);
    }

    public void setLowerStagePower(double elevatorPower) {
        m_lowerStageMotor.set(ControlMode.PercentOutput, elevatorPower);
    }

    public void setUpperStagePower(double actuatorPower) {
        m_upperStageMotor.set(ControlMode.PercentOutput, actuatorPower);
    }

    public void setLowerStagePosition(double position) {
        m_lowerStageMotor.set(ControlMode.Position, position);
    }
    
    public void setUpperStagePosition(double position) {
        System.out.println("setting position to: " + position);
        m_upperStageMotor.set(ControlMode.Position, position);
    }

    public void setPosition(double position) {
        System.out.println("Setting position to: " + position + "ED: " + position/3 + " AD: " + (position*2/3));
        if (position > 0 && position < (double)(MAX_HEIGHT + MAX_UPPER_STAGE_HEIGHT)) {
            setLowerStagePosition((2*position)/3);
            setUpperStagePosition((position/3));
            targetPosition = position;
        } else {
            System.out.println("im a bot");
        }
    }

    public void addToPosition(double add) {
        if (targetPosition < 0) {
            targetPosition = getLowerStageHeight() + getUpperStageHeight();
        }
        targetPosition += add;
        m_lowerStageMotor.set(ControlMode.Position, targetPosition);
    }

    public double getUpperStageHeight() {
       return m_upperStageMotor.getSensorPosition();
    }

    public double getLowerStageHeight() {
       return m_lowerStageMotor.getSensorPosition();
    }

    public void resetElevatorEncoder() {
        m_lowerStageMotor.setPosition(0);
    }

    public void resetActuatorEncoder() {
        m_upperStageMotor.setPosition(0);
    }

    public void setLowerStageHeight(double position, double power) {
        while (getLowerStageHeight() != position) {
            if (getLowerStageHeight() > position) {
                setLowerStagePower(-power);
            } else {
                setLowerStagePower(power);
            }
        }
    }

    public void setUpperStageHeight(double position, double power) {
        while (getUpperStageHeight() != position) {
            if (getUpperStageHeight() > position) {
                setUpperStagePower(-power);
            } else {
                setUpperStagePower(power);
            }
        }
    }

    public void lowerStageUp() {
        targetPosition = -1;
        System.out.println("EH: " + Robot.elevator.getLowerStageHeight() + " AH: " + Robot.elevator.getUpperStageHeight());
        if (Robot.elevator.getLowerStageHeight() > NEAR_MAX_HEIGHT) {
            if (Robot.elevator.getLowerStageHeight() >= MAX_HEIGHT) {
                Robot.elevator.setLowerStagePower(0.0);
            } else {
                Robot.elevator.setLowerStagePower(0.2);
            }
            if (Robot.elevator.getUpperStageHeight() > MAX_UPPER_STAGE_HEIGHT) {
                Robot.elevator.setUpperStagePower(0.0);
            } else if (Robot.elevator.getUpperStageHeight() > NEAR_MAX_UPPER_STAGE_HEIG) {
                Robot.elevator.setUpperStagePower(0.3);
            } else {
                Robot.elevator.setUpperStagePower(0.75);
            }
        } else {
            Robot.elevator.setLowerStagePower(0.75);
        }
    }

    public void lowerStageDown() {
        targetPosition = -1;
        System.out.println("EH: " + Robot.elevator.getLowerStageHeight() + " AH: " + Robot.elevator.getUpperStageHeight());
        if (Robot.elevator.getUpperStageHeight() < NEAR_MIN_ACTUATOR_HEIGHT) {
            if (Robot.elevator.getLowerStageHeight() < MIN_HEIGHT) {
                Robot.elevator.setLowerStagePower(0.0);
            } else if (Robot.elevator.getLowerStageHeight() < NEAR_MIN_HEIGHT) {
                Robot.elevator.setLowerStagePower(-0.2);
            }  else {
                Robot.elevator.setLowerStagePower(-0.75);
            }
            if (Robot.elevator.getUpperStageHeight() <= MIN_HEIGHT_ACTUATOR) {
                Robot.elevator.setUpperStagePower(0.0);
            } else {
                Robot.elevator.setUpperStagePower(-0.2);
            }
        } else {
            Robot.elevator.setUpperStagePower(-0.75);
        }
    }

    public void lowerStageNeutral() {
        if (targetPosition < 0.0) {
            Robot.elevator.setUpperStagePower(0.0);
            Robot.elevator.setLowerStagePower(0.0);
            targetPosition = getLowerStageHeight() + getUpperStageHeight();
        }
    }




    /**
    * Move the elevator motor a set number of inches.
    **/
    public void moveLowerStageDistance(double distance) {
        m_lowerStageMotor.set(ControlMode.Position, distance / DIST_PER_PULSE);
    }

    /**
    * Move the gripper motor a set number of inches.
    **/
    public void moveUpperStageDistance(double distance) {
        m_upperStageMotor.set(ControlMode.Position, distance / DIST_PER_PULSE);
    }
}

