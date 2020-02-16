package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.TurnAround;

public enum AutonomousModes {
    
    @AutonomousCommand(commandClass = TurnAround.class) Testing,
}
