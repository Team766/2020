package com.team766.frc2020.mechanisms;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.team766.controllers.PIDController;
import com.team766.config.ConfigFileReader;
import com.team766.framework.Mechanism;
import com.team766.frc2020.Robot;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.hal.DigitalInputReader;
import com.team766.hal.SolenoidController;
// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.frc2020.mechanisms.LightSensor;

public class WaterWheel extends Mechanism {

    private CANSpeedController m_talon;
    private final SolenoidController m_ballPusher;
   //private final CANSpeedController wheelState;
    private DigitalInputReader wheelLimitSwitch;
    private CANSpeedController m_wheelMotor;
    public boolean intakeMode = false;
    public boolean outtakeMode = true;
    private double initialWaterWheelPosition;

    
    public WaterWheel() {
        //m_talon = RobotProvider.instance.getTalonCANMotor("waterwheel.talon");
        m_ballPusher = RobotProvider.instance.getSolenoid("waterwheel.pusher");
        //wheelState = RobotProvider.instance.getTalonCANMotor("waterwheel.motor");
        // m_wheelMotor = new WPI_TalonSRX(ConfigFileReader.getInstance().getInt("waterwheel.motor").get());
        m_wheelMotor = RobotProvider.instance.getTalonCANMotor("waterwheel.motor");
        m_wheelMotor.configMotionCruiseVelocity(1000);
        m_wheelMotor.configMotionAcceleration(800);
        m_wheelMotor.config_kP(0, 1.5, 0);
        m_wheelMotor.config_kI(0, 0, 0);
        m_wheelMotor.config_kD(0, 0, 0);
        m_wheelMotor.config_kF(0, 0, 0);
        initialWaterWheelPosition = m_wheelMotor.getSensorPosition(); 

        
        //m_wheelMotor.set(ControlMode.MotionMagic, 0.0);

        //wheelLimitSwitch = RobotProvider.instance.getDigitalInput("waterwheel.limitswitch");
    }

    public void setIntakeMode(boolean mode) {
        if(!(intakeMode == mode)) {  
            intakeMode = mode;  
            setWheelPosition(Robot.waterwheel.getWheelPosition() + 420);
        }
    }

    public void setOuttakeMode(boolean mode) {
        if(!(outtakeMode == mode)) {
            outtakeMode = mode;
            setWheelPosition(Robot.waterwheel.getWheelPosition() + 0);
        }
    }

    public void setWheelPower(final double wheelPower) {
        m_wheelMotor.set(ControlMode.MotionMagic, wheelPower);
    }

    public void setWheelPosition(double position) {
        //example: _talonRght.set(ControlMode.MotionMagic, targetDistance, DemandType.AuxPID, desiredRobotHeading);
        m_wheelMotor.set(ControlMode.MotionMagic, position);
    }

    public void resetWheelPosition() {
        m_wheelMotor.setPosition(0);
    }

    public void initializeWheelPosition() {
        m_wheelMotor.setPosition(1000);
        m_wheelMotor.set(ControlMode.Velocity, 1);
        // while (not hall effect sensor){}
        m_wheelMotor.set(ControlMode.Velocity, 1);

    }

    public void setPusherState(final boolean state) {
        m_ballPusher.set(state);
    }

    public boolean isPusherOut() {
        return m_ballPusher.get();
    }

    public void pusherOutAndIn() {
        while (Robot.lightSensor.getTopLightSensorState()) {
            m_ballPusher.set(true);
        }
        m_ballPusher.set(false);
    }

    public void setInitialWaterWheelPosition() {
        initialWaterWheelPosition = m_wheelMotor.getSensorPosition();
    }

    public double getWheelPosition() {
        return m_wheelMotor.getSensorPosition() - initialWaterWheelPosition;
    }

    public void setWheelVelocity(final double wheelVelocity) {
        m_wheelMotor.set(ControlMode.Velocity, wheelVelocity);
    }


    public void turnDegrees(int degrees) {
        if (!isPusherOut()) {
            System.out.println("turning degrees:" + degrees);
            int currentPosition = (int)(m_wheelMotor.getSensorPosition());
            m_wheelMotor.set(ControlMode.Position, currentPosition + degrees);
        }
        System.out.println("WARNING PUSHER WAS OUT! DID NOT TURN WATERWHEEL");

    }

}
