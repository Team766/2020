package com.team766.frc2019.mechanisms;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team766.framework.Mechanism;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController;
import com.team766.hal.DigitalInputReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;



public class NewElevator extends Mechanism {

    private CANSpeedController m_lowerElevatorMotor;
    private CANSpeedController m_upperElevatorMotor;
    private DigitalInputReader lowerElevatorMinLimitSwitch;
    private DigitalInputReader lowerElevatorMaxLimitSwitch;
    private DigitalInputReader upperElevatorMinLimitSwitch;
    private DigitalInputReader upperElevatorMaxLimitSwitch;
    public static double DIST_PER_PULSE = Robot.drive.DIST_PER_PULSE;

    public static int LVL1 = 5000;
    public static int LVL2 = 1115000;
    public static int LVL3 = 2150000;

    public NewElevator() {
        m_lowerElevatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.elevatorMotor");
        m_upperElevatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.actuatorMotor");
        lowerElevatorMinLimitSwitch = RobotProvider.instance.getDigitalInput("elevator.lowerMinLimitSwitch");
        lowerElevatorMaxLimitSwitch = RobotProvider.instance.getDigitalInput("elevator.lowerMaxLimitSwitch");
        upperElevatorMinLimitSwitch = RobotProvider.instance.getDigitalInput("elevator.upperMinLimitSwitch");
        upperElevatorMaxLimitSwitch = RobotProvider.instance.getDigitalInput("elevator.upperMaxLimitSwitch");
        /*m_lowerElevatorMotor.configNominalOutputForward(0.0);
        m_lowerElevatorMotor.configNominalOutputReverse(0.0);
        m_lowerElevatorMotor.configPeakOutputForward(1.0);
        m_lowerElevatorMotor.configPeakOutputReverse(-1.0);
        m_upperElevatorMotor.configNominalOutputForward(0.0);
        m_upperElevatorMotor.configNominalOutputReverse(0.0);
        m_upperElevatorMotor.configPeakOutputForward(1.0);
        m_upperElevatorMotor.configPeakOutputReverse(-1.0);
        m_lowerElevatorMotor.configFactoryDefault();
        m_upperElevatorMotor.configFactoryDefault(); probably useless*/
        m_upperElevatorMotor.setInverted(true);
        m_lowerElevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_upperElevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_lowerElevatorMotor.setSensorPhase(true);
        m_upperElevatorMotor.setSensorPhase(false);
        m_lowerElevatorMotor.config_kP(0, 0.01, 0);
        m_lowerElevatorMotor.config_kI(0, 0.0, 0);
        m_lowerElevatorMotor.config_kD(0, 0.01, 0);
        m_lowerElevatorMotor.config_kF(0, 0.0, 0);
        m_upperElevatorMotor.config_kP(0, 0.01, 0);
        m_upperElevatorMotor.config_kI(0, 0.0, 0);
        m_upperElevatorMotor.config_kD(0, 0.01, 0);
        m_upperElevatorMotor.config_kF(0, 0.0, 0);
        
        m_lowerElevatorMotor.setNeutralMode(NeutralMode.Brake);
        m_upperElevatorMotor.setNeutralMode(NeutralMode.Brake);
        m_upperElevatorMotor.setPosition(0);
        m_lowerElevatorMotor.setPosition(0);
    }

    // Limit switch code
    
    public void hover() {
        Robot.elevator.setLowerPosition(Math.max(Robot.elevator.getLowerHeight(),0.0));
    }

    public boolean getLowerMinLimitSwitch() {
        return lowerElevatorMinLimitSwitch.get();
    }

    public boolean getLowerMaxLimitSwitch() {
        return lowerElevatorMaxLimitSwitch.get();
    }

    public boolean getUpperMinLimitSwitch() {
        return upperElevatorMinLimitSwitch.get();
    }

    public boolean getUpperMaxLimitSwitch() {
        return upperElevatorMaxLimitSwitch.get();
    }


    public void resetUpperEncoder() {
        m_upperElevatorMotor.setPosition(0);
    }

    public void resetLowerEncoder() {
        m_lowerElevatorMotor.setPosition(0);
    }

    public void setElevatorPosition(double targetPosition) {
        double upperTarget = 3*targetPosition/7;
        double lowerTarget = 4*targetPosition/7;
        m_lowerElevatorMotor.set(ControlMode.MotionMagic, lowerTarget);
        m_upperElevatorMotor.set(ControlMode.MotionMagic, upperTarget);
    }
}

