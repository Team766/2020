package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class TurnOnce extends Subroutine {
    public TurnOnce() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        callSubroutine(new PreciseTurnRadius(90, 3, .75, 0));
    }
    
}
