package com.team766.frc2020;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2020.commands.PathRunner;
import com.team766.frc2020.commands.IntakeBall;
import com.team766.frc2020.commands.WebSocketTest;
import com.team766.frc2020.commands.LimeScore;
import com.team766.frc2020.commands.NextBall;
import com.team766.frc2020.commands.TurnAround;
import com.team766.frc2020.commands.DriveSquare;
// import com.team766.frc2020.commands.NextBall;

public enum AutonomousModes {
    
    @AutonomousCommand(commandClass = NextBall.class) NextBall,
    @AutonomousCommand(commandClass = IntakeBall.class) IntakeBall,
    @AutonomousCommand(commandClass = TurnAround.class) TurnAround,

    // @AutonomousCommand(commandClass = NextBall.class) NextBall,
    @AutonomousCommand(commandClass = PathRunner.class) PathRunner,
    @AutonomousCommand(commandClass = LimeScore.class)LimeScore,
    @AutonomousCommand(commandClass = WebSocketTest.class) WebSocketTest,
    @AutonomousCommand(commandClass = DriveSquare.class) DriveSquare,


}
