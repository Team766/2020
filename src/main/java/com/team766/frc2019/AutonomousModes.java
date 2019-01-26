package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.*;

public enum AutonomousModes {
	@AutonomousCommand(commandClass= LimeLogger.class)
	LimeLogger,
	@AutonomousCommand(commandClass= DriveSquare.class)
	DriveSquare,
	@AutonomousCommand(commandClass= FollowDumb.class)
	FollowDumb,
}