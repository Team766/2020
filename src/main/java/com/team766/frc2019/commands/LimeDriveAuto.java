package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.mechanisms.LimeLightI;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class LimeDriveAuto extends Subroutine {

    LimeLightI limeLight;

    public LimeDriveAuto() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("LimeDrive STARTING");
        Robot.drive.nukeRobot();

            callSubroutine(new LimeDrive(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider()));
    }
}