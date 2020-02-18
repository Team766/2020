package com.team766.frc2020;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2020.commands.TurnAround;

public enum AutonomousModes {
    
    @AutonomousCommand(commandClass = TurnAround.class) TurnAround,
}
