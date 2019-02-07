package com.team766.frc2019.mechanisms;

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
        m_elevatorMotor = RobotProvider.instance.getCANMotor("elevator.elevatorMotor");
        m_actuatorMotor = RobotProvider.instance.getCANMotor("elevator.actuatorMotor"); 
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

