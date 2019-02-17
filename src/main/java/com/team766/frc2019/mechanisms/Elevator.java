package com.team766.frc2019.mechanisms;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team766.framework.Mechanism;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;


public class Elevator extends Mechanism {

    private CANSpeedController m_elevatorMotor;
    private CANSpeedController m_actuatorMotor;
    public static double DIST_PER_PULSE = Robot.drive.DIST_PER_PULSE;
    public static double targetPosition;

    private static int MIN_HEIGHT = 0;
	private static int NEAR_MIN_HEIGHT = 75000;
	private static int MAX_HEIGHT = 2130000;
    private static int NEAR_MAX_HEIGHT = 2030000;
    private static int MAX_HEIGHT_ACTUATOR = 950000;
	private static int NEAR_MAX_ACTUATOR_HEIGHT = 930000;
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
        m_elevatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.elevatorMotor");
        m_actuatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.actuatorMotor");
        m_elevatorMotor.configFactoryDefault();
        m_actuatorMotor.configFactoryDefault();
        m_actuatorMotor.setInverted(true);
        m_elevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_actuatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_elevatorMotor.setSensorPhase(true);
        m_actuatorMotor.setSensorPhase(false);
        m_elevatorMotor.configNominalOutputForward(0.0);
        m_elevatorMotor.configNominalOutputReverse(0.0);
        m_elevatorMotor.configPeakOutputForward(1.0);
        m_elevatorMotor.configPeakOutputReverse(-1.0);
        m_actuatorMotor.configNominalOutputForward(0.0);
        m_actuatorMotor.configNominalOutputReverse(0.0);
        m_actuatorMotor.configPeakOutputForward(1.0);
        m_actuatorMotor.configPeakOutputReverse(-1.0);
        m_elevatorMotor.config_kP(0, 0.05, 0);
        m_elevatorMotor.config_kI(0, 0.0, 0);
        m_elevatorMotor.config_kD(0, 0.01, 0);
        m_elevatorMotor.config_kF(0, 0.0, 0);
        m_actuatorMotor.config_kP(0, 0.05, 0);
        m_actuatorMotor.config_kI(0, 0.0, 0);
        m_actuatorMotor.config_kD(0, 0.01, 0);
        m_actuatorMotor.config_kF(0, 0.0, 0);
        m_elevatorMotor.setNeutralMode(NeutralMode.Brake);
        m_actuatorMotor.setNeutralMode(NeutralMode.Brake);
        m_actuatorMotor.setPosition(0);
        m_elevatorMotor.setPosition(0);
    }

    public void setElevatorPower(double elevatorPower) {
        m_elevatorMotor.set(ControlMode.PercentOutput, elevatorPower);
    }

    public void setActuatorPower(double actuatorPower) {
        m_actuatorMotor.set(ControlMode.PercentOutput, actuatorPower);
    }

    public void setElevatorPosition(double position) {
        m_elevatorMotor.set(ControlMode.Position, position);
    }
    
    public void setActuatorPosition(double position) {
        System.out.println("setting position to: " + position);
        m_actuatorMotor.set(ControlMode.Position, position);
    }

    public void setPosition(double position) {
        System.out.println("Setting position to: " + position + "ED: " + position/3 + " AD: " + (position*2/3));
        if (position > 0 && position < (double)(MAX_HEIGHT + MAX_HEIGHT_ACTUATOR)) {
            setElevatorPosition((2*position)/3);
            setActuatorPosition((position/3));
            targetPosition = position;
        } else {
            System.out.println("im a bot");
        }
    }

    public void addToPosition(double add) {
        if (targetPosition < 0) {
            targetPosition = getElevatorHeight() + getActuatorHeight();
        }
        targetPosition += add;
        m_elevatorMotor.set(ControlMode.Position, targetPosition);
    }

    public double getActuatorHeight() {
       return m_actuatorMotor.getSensorPosition();
    }

    public double getElevatorHeight() {
       return m_elevatorMotor.getSensorPosition();
    }

    public void resetElevatorEncoder() {
        m_elevatorMotor.setPosition(0);
    }

    public void resetActuatorEncoder() {
        m_actuatorMotor.setPosition(0);
    }

    public void setElevatorHeight(double position, double power) {
        while (getElevatorHeight() != position) {
            if (getElevatorHeight() > position) {
                setElevatorPower(-power);
            } else {
                setElevatorPower(power);
            }
        }
    }

    public void setActuatorHeight(double position, double power) {
        while (getActuatorHeight() != position) {
            if (getActuatorHeight() > position) {
                setActuatorPower(-power);
            } else {
                setActuatorPower(power);
            }
        }
    }

    public void elevatorUp() {
        targetPosition = -1;
        System.out.println("EH: " + Robot.elevator.getElevatorHeight() + " AH: " + Robot.elevator.getActuatorHeight());
        if (Robot.elevator.getElevatorHeight() > NEAR_MAX_HEIGHT) {
            if (Robot.elevator.getElevatorHeight() >= MAX_HEIGHT) {
                Robot.elevator.setElevatorPower(0.0);
            } else {
                Robot.elevator.setElevatorPower(0.2);
            }
            if (Robot.elevator.getActuatorHeight() > MAX_HEIGHT_ACTUATOR) {
                Robot.elevator.setActuatorPower(0.0);
            } else if (Robot.elevator.getActuatorHeight() > NEAR_MAX_ACTUATOR_HEIGHT) {
                Robot.elevator.setActuatorPower(0.3);
            } else {
                Robot.elevator.setActuatorPower(0.75);
            }
        } else {
            Robot.elevator.setElevatorPower(0.75);
        }
    }

    public void elevatorDown() {
        targetPosition = -1;
        System.out.println("EH: " + Robot.elevator.getElevatorHeight() + " AH: " + Robot.elevator.getActuatorHeight());
        if (Robot.elevator.getActuatorHeight() < NEAR_MIN_ACTUATOR_HEIGHT) {
            if (Robot.elevator.getElevatorHeight() < MIN_HEIGHT) {
                Robot.elevator.setElevatorPower(0.0);
            } else if (Robot.elevator.getElevatorHeight() < NEAR_MIN_HEIGHT) {
                Robot.elevator.setElevatorPower(-0.2);
            }  else {
                Robot.elevator.setElevatorPower(-0.75);
            }
            if (Robot.elevator.getActuatorHeight() <= MIN_HEIGHT_ACTUATOR) {
                Robot.elevator.setActuatorPower(0.0);
            } else {
                Robot.elevator.setActuatorPower(-0.2);
            }
        } else {
            Robot.elevator.setActuatorPower(-0.75);
        }
    }

    public void elevatorNeutral() {
        if (targetPosition < 0.0) {
            Robot.elevator.setActuatorPower(0.0);
            Robot.elevator.setElevatorPower(0.0);
            targetPosition = getElevatorHeight() + getActuatorHeight();
        }
    }




    /**
    * Move the elevator motor a set number of inches.
    **/
    public void moveElevatorDistance(double distance) {
        m_elevatorMotor.set(ControlMode.Position, distance / DIST_PER_PULSE);
    }

    /**
    * Move the gripper motor a set number of inches.
    **/
    public void moveActuatorDistance(double distance) {
        m_actuatorMotor.set(ControlMode.Position, distance / DIST_PER_PULSE);
    }
}

