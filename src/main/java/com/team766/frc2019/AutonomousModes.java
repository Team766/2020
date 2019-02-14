package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.*;

public enum AutonomousModes {
	@AutonomousCommand(commandClass= FindAngle.class) FindAngle,
    @AutonomousCommand(commandClass= DriveSquare.class) DriveSquare,
    @AutonomousCommand(commandClass= ExtendGripper.class) ExtendGripper,
    @AutonomousCommand(commandClass= RetractGripper.class) RetractGripper,
    @AutonomousCommand(commandClass= TestGripper.class) TestGripper,
    @AutonomousCommand(commandClass= DriveVelocity.class) DriveVelocity,
    @AutonomousCommand(commandClass= DrivePercent.class) DrivePercent
}
