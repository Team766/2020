package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;

public enum AutonomousModes {
    @AutonomousCommand(commandClass= com.team766.frc2019.commands.DriveStraight.class)
    DriveStraight,
    @AutonomousCommand(commandClass= com.team766.frc2019.commands.DriveSquare.class)
    DriveSquare,
    @AutonomousCommand(commandClass= com.team766.frc2019.commands.PreciseTurn.class)
    PreciseTurn,

}
