package com.team766.frc2020;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2020.commands.LimeScore;


public enum AutonomousModes {
    @AutonomousCommand(commandClass = LimeScore.class) LimeScore,
}
