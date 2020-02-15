package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController;
import com.team766.hal.RobotProvider;
import com.team766.hal.SolenoidController;
import com.team766.hal.CANSpeedController.ControlMode;

public class Spinner extends Mechanism {
    
    private CANSpeedController wheelMotor;
    private SolenoidController extensionPiston;
    private SolenoidController stoppingPiston;

    public Spinner() {
        wheelMotor = RobotProvider.instance.getVictorCANMotor("spinner.wheelMotor");
        extensionPiston = RobotProvider.instance.getSolenoid("spinner.wheelMotor");
        stoppingPiston = RobotProvider.instance.getSolenoid("spinner.wheelMotor");
    }

    public void setWheelPower(double wheelPower) {
        wheelMotor.set(ControlMode.PercentOutput, wheelPower);
    }

    public void setExtensionState(boolean newState) {
        extensionPiston.set(newState);
    }

    public void setStoppingState(boolean newState) {
        stoppingPiston.set(newState);
    }
}
