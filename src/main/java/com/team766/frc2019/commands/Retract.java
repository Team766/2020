package com.team766.frc2019.commands;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

public class Retract extends Subroutine {
    public Retract(){
        takeControl(Robot.flowerGripper);
    }

   
    @Override
    protected void subroutine() {
        Robot.flowerActuator.retract();
    }
}