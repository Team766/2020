package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.Debug;
import com.team766.frc2019.commands.LimeDebug;
import com.team766.frc2019.commands.TeleopAuton;
import com.team766.frc2019.commands.LeftHab2RocketPlayerStationBackRocket;
import com.team766.frc2019.commands.AutonRocketTeleop;
import com.team766.frc2019.commands.LimePickupAuto;
import com.team766.frc2019.commands.LimeScoreAuto;
import com.team766.frc2019.commands.TurnAround;
import com.team766.frc2019.commands.DriveAuto1;
import com.team766.frc2019.commands.DriveAuto2;
import com.team766.frc2019.commands.RightLvl1RightSideCargo;
import com.team766.frc2019.commands.MiddleLvl1StraightCargo;
import com.team766.frc2019.commands.DriveVelocity;

public enum AutonomousModes {
    //commands
    @AutonomousCommand(commandClass = TurnAround.class) TurnAround,
    @AutonomousCommand(commandClass = com.team766.frc2019.commands.LeftHab2RocketPlayerStationBackRocket.class) LeftHab2RocketPlayerStationBackRocket,

    //engage limelight
    @AutonomousCommand(commandClass= LimeScoreAuto.class) LimeScoreAuto,
    @AutonomousCommand(commandClass= LimePickupAuto.class) LimePickupAuto,
    //teleop auton
    @AutonomousCommand(commandClass= TeleopAuton.class) TeleopAuton,
    @AutonomousCommand(commandClass= AutonRocketTeleop.class) AutonRocketTeleop,
    //auton paths
    @AutonomousCommand(commandClass= DriveAuto1.class) DriveAuto1,
    @AutonomousCommand(commandClass= DriveAuto2.class) DriveAuto2,
    @AutonomousCommand(commandClass= RightLvl1RightSideCargo.class) RightLvl1RightSideCargo,
    @AutonomousCommand(commandClass= MiddleLvl1StraightCargo.class) MiddleLvl1StraightCargo,

    //debug
    @AutonomousCommand(commandClass= Debug.class) Debug,
    @AutonomousCommand(commandClass= LimeDebug.class) LimeDebug,
    @AutonomousCommand(commandClass= DriveVelocity.class) DriveVelocity,
}
