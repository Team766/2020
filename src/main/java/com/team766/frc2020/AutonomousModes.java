package com.team766.frc2020;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2020.commands.TurnAround;
import com.team766.frc2020.commands.PathRunner;


public enum AutonomousModes {
    
    @AutonomousCommand(commandClass = PathRunner.class) PathRunner,


}
