package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;

public enum AutonomousModes {
    @AutonomousCommand(commandClass= com.team766.frc2019.mechanisms.DriveStraight.class)
    DriveStraight,
}
