package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;

public class DriveSquare extends Subroutine {
    protected void subroutine() {
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new TurnLeft());
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new TurnLeft());
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new TurnLeft());
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new TurnLeft());
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new TurnLeft());
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new TurnLeft());
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new TurnLeft());
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
    }
}