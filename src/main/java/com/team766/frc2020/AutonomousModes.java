package com.team766.frc2020;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2020.commands.*;

public enum AutonomousModes {
    @AutonomousCommand(commandClass = PathRunner.class) PathRunner,

    @AutonomousCommand(commandClass = WebSocketTest.class) WebSocketTest,

}
