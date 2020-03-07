package com.team766.frc2020.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;


public class IntakeBall extends Subroutine {
    protected void subroutine() {
        Robot.intake.setIntakeState(true);
        Robot.intake.setIntakePower(0.001);
        Robot.wagon.setWagonPower(0.001);
        // if (Robot.ultrasonic.isBallPresent()){
        //     System.out.println("Ball is present currently in the waterwheel");
        // } else {
        //     Robot.waterwheel.oneTurn();
        // }


        double start = Robot.waterwheel.getWheelPosition();
        while(Math.abs(Robot.waterwheel.getWheelPosition()-start)<100){
            System.out.println("waterstatus " + Math.abs(Robot.waterwheel.getWheelPosition()-start));
        } 
        
        // retract all this once at end
        Robot.intake.setIntakeState(false);
        Robot.intake.setIntakePower(0);
        Robot.wagon.setWagonPower(0);
        // Robot.waterwheel.oneTurn();
    }

}