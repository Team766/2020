package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;

public class DriveSquare extends Subroutine {
    protected void subroutine() {
        callSubroutine(new DriveStraight(1.0));

        callSubroutine(new TurnLeft(1.0));

        callSubroutine(new DriveStraight(1.0));

        callSubroutine(new TurnLeft(1.0));

        callSubroutine(new DriveStraight(1.0));

        callSubroutine(new TurnLeft(1.0));

        callSubroutine(new DriveStraight(1.0));

        callSubroutine(new TurnLeft(1.0));

        callSubroutine(new DriveStraight(1.0));

        callSubroutine(new TurnLeft(1.0));

        callSubroutine(new DriveStraight(1.0));

        callSubroutine(new TurnLeft(1.0));

        callSubroutine(new DriveStraight(1.0));

        callSubroutine(new TurnLeft(1.0));

        callSubroutine(new DriveStraight(1.0));
    }
}