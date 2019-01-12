package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.DriveStraight;
import com.team766.frc2019.commands.TurnLeft;
import com.team766.frc2019.commands.DriveSquare;

public enum AutonomousModes {
    @AutonomousCommand(commandClass= DriveStraight.class)
    DriveStraight,
    @AutonomousCommand(commandClass= TurnLeft.class)
    TurnLeft,
    @AutonomousCommand(commandClass= DriveSquare.class)
    DriveSquare,
}
