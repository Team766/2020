package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveSquare extends Subroutine {
    protected void subroutine() {
        System.out.println(Robot.drive.getGyroAngle());
        callSubroutine(new PreciseDrive(2));
        waitForSeconds(0.5);
        callSubroutine(new PreciseTurn(90));
        System.out.println(Robot.drive.getGyroAngle());
        /*for (int i = 0; i < 8; i++) {
            callSubroutine(new PreciseDrive(3));
            waitForSeconds(0.5);
            callSubroutine(new PreciseTurn(90));
            waitForSeconds(0.5);
        }*/
        /*callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new PreciseTurn(90));
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new PreciseTurn(90));
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new PreciseTurn(90));
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new PreciseTurn(90));
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new PreciseTurn(90));
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new PreciseTurn(90));
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);
        callSubroutine(new PreciseTurn(90));
        waitForSeconds(0.5);
        callSubroutine(new DriveStraight());
        waitForSeconds(0.5);*/
    }
}