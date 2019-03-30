package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.Debug;
import com.team766.frc2019.commands.LimeDebug;
import com.team766.frc2019.commands.TeleopAuton;
import com.team766.frc2019.commands.LeftHab2RocketPlayerStationBackRocket;
import com.team766.frc2019.commands.RightHab2RocketPlayerStationBackRocket;
import com.team766.frc2019.commands.AutonRocketTeleop;
import com.team766.frc2019.commands.LimePickupAuto;
import com.team766.frc2019.commands.LimeScoreAuto;
import com.team766.frc2019.commands.TurnAround;
import com.team766.frc2019.commands.LeftHab2SideCargoPlayerStationSideCargo;
import com.team766.frc2019.commands.RightHab2SideCargoPlayerStationSideCargo;
import com.team766.frc2019.commands.MiddleLvl1StraightCargoRight;
import com.team766.frc2019.commands.MiddleLvl1StraightCargoLeft;
//import com.team766.frc2019.commands.MiddleLvl1StraightCargo;
import com.team766.frc2019.commands.DriveVelocity;

public enum AutonomousModes {
    //commands
    @AutonomousCommand(commandClass = TurnAround.class) TurnAround, 

    //engage limelight
    @AutonomousCommand(commandClass= LimeScoreAuto.class) LimeScoreAuto,
    @AutonomousCommand(commandClass= LimePickupAuto.class) LimePickupAuto,
    //teleop auton
    @AutonomousCommand(commandClass= TeleopAuton.class) TeleopAuton,
    @AutonomousCommand(commandClass= AutonRocketTeleop.class) AutonRocketTeleop,
    //auton paths

    @AutonomousCommand(commandClass = LeftHab2RocketPlayerStationBackRocket.class) LeftHab2RocketPlayerStationBackRocket,
    @AutonomousCommand(commandClass = RightHab2SideCargoPlayerStationSideCargo.class) RightHab2SideCargoPlayerStationSideCargo,
    @AutonomousCommand(commandClass = LeftHab2SideCargoPlayerStationSideCargo.class) LeftHab2SideCargoPlayerStationSideCargo,
    @AutonomousCommand(commandClass = RightHab2RocketPlayerStationBackRocket.class) RightHab2RocketPlayerStationBackRocket,
    @AutonomousCommand(commandClass = MiddleLvl1StraightCargoRight.class) MiddleLvl1StraightCargoRight,
    @AutonomousCommand(commandClass = MiddleLvl1StraightCargoLeft.class) MiddleLvl1StraightCargoLeft,


   // @AutonomousCommand(commandClass= MiddleLvl1StraightCargo.class) MiddleLvl1StraightCargo,

    //debug
    @AutonomousCommand(commandClass= Debug.class) Debug,
    @AutonomousCommand(commandClass= LimeDebug.class) LimeDebug,
    @AutonomousCommand(commandClass= DriveVelocity.class) DriveVelocity,
}
