package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.*;

public enum AutonomousModes {
    @AutonomousCommand(commandClass = TurnAround.class) TurnAround,
    @AutonomousCommand(commandClass = WebSocketTest.class) WebSocketTest,

}
