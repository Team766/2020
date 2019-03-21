package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.mechanisms.LimeLightI;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class TurnAround extends Subroutine {

    public TurnAround() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("TurnAround STARTING");

        callSubroutine(new PreciseTurn((Robot.drive.getGyroAngle() + 180) % 360));
        
    }
}