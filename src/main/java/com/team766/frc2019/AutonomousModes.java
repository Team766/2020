package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.TurnAround;
import com.team766.frc2019.commands.WebSocketTest;

public enum AutonomousModes {
    @AutonomousCommand(commandClass = TurnAround.class) TurnAround,
    @AutonomousCommand(commandClass = WebSocketTest.class) WebSocketTest,
}
