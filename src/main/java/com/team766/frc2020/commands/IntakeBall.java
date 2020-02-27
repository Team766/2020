package com.team766.frc2020.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;


public class IntakeBall extends Subroutine {
    protected void subroutine() {
        Robot.intake.setIntakeState(true);
        Robot.intake.setIntakePower(1.0);
        Robot.wagon.setWagonPower(1.0);
        if (Robot.ultrasonic.isBallPresent()){
            System.out.println("Ball is present currently in the waterwheel");
        } else {
            Robot.waterwheel.oneTurn();
        }

        // retract all this once the trigger is released

        // Robot.intake.setIntakeState(false);
        // Robot.intake.setIntakePower(0);
        // Robot.wagon.setWagonPower(0);
        // Robot.waterwheel.oneTurn();
    }

}