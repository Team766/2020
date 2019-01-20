package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.DriveStraight;
import com.team766.frc2019.commands.DriveSquare;
import com.team766.frc2019.commands.TurnLeft;;
import com.team766.frc2019.commands.TurnLeft;
import com.team766.frc2019.commands.ExtendGripper;
import com.team766.frc2019.commands.RetractGripper;
import com.team766.frc2019.commands.TestActuator;
import com.team766.frc2019.commands.TestGripper;

public enum AutonomousModes {
    @AutonomousCommand(commandClass= DriveStraight.class)
    DriveStraight, 
    @AutonomousCommand(commandClass= DriveSquare.class)
    DriveSquare,
    @AutonomousCommand(commandClass= TurnLeft.class)
    TurnLeft,
    @AutonomousCommand(commandClass= ExtendGripper.class) ExtendGripper,
    @AutonomousCommand(commandClass= RetractGripper.class) RetractGripper,
    @AutonomousCommand(commandClass= TestGripper.class) TestGripper,
    @AutonomousCommand(commandClass= TestActuator.class) TestActuator,
}
