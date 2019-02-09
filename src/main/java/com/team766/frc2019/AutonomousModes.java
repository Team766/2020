package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.DriveStraight;
import com.team766.frc2019.commands.DriveSquare;
import com.team766.frc2019.commands.DriveSquareOther;
import com.team766.frc2019.commands.PreciseDrive;
import com.team766.frc2019.commands.ExtendGripper;
import com.team766.frc2019.commands.RetractGripper;
import com.team766.frc2019.commands.TestGripper;
import com.team766.frc2019.commands.DriveVelocity;
import com.team766.frc2019.commands.DrivePercent;

public enum AutonomousModes {
    @AutonomousCommand(commandClass= DriveSquare.class) DriveSquare,
    @AutonomousCommand(commandClass= DriveSquareOther.class) DriveSquareOther,
    @AutonomousCommand(commandClass= ExtendGripper.class) ExtendGripper,
    @AutonomousCommand(commandClass= RetractGripper.class) RetractGripper,
    @AutonomousCommand(commandClass= TestGripper.class) TestGripper,
    @AutonomousCommand(commandClass= DriveVelocity.class) DriveVelocity,
    @AutonomousCommand(commandClass= DrivePercent.class) DrivePercent,
    // @AutonomousCommand(commandClass= TestActuator.class) TestActuator,
}
