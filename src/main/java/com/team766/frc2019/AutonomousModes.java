package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.DriveStraight;
import com.team766.frc2019.commands.DriveSquare;
import com.team766.frc2019.commands.TurnLeft;;

public enum AutonomousModes {
    @AutonomousCommand(commandClass= DriveStraight.class)
    DriveStraight, 
    @AutonomousCommand(commandClass= DriveSquare.class)
    DriveSquare,
    @AutonomousCommand(commandClass= TurnLeft.class)
    TurnLeft,

    
}
