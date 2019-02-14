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

    public Elevator() {
        m_elevatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.elevatorMotor");
        m_actuatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.actuatorMotor");
        m_elevatorMotor.configFactoryDefault();
        m_actuatorMotor.configFactoryDefault();
        m_elevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_actuatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_elevatorMotor.setSensorPhase(true);
        m_actuatorMotor.setSensorPhase(true);
        m_elevatorMotor.configNominalOutputForward(0);
        m_elevatorMotor.configNominalOutputReverse(0);
        m_elevatorMotor.configPeakOutputForward(1);
        m_elevatorMotor.configPeakOutputReverse(-1);
        m_actuatorMotor.configNominalOutputForward(0);
        m_actuatorMotor.configNominalOutputReverse(0);
        m_actuatorMotor.configPeakOutputForward(1);
        m_actuatorMotor.configPeakOutputReverse(-1);
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
    }

    public void setElevatorPower(double elevatorPower) {
        m_elevatorMotor.set(ControlMode.PercentOutput, elevatorPower);
    }
    public void setActuatorPower(double actuatorPower) {
        m_actuatorMotor.set(ControlMode.PercentOutput, actuatorPower);
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

