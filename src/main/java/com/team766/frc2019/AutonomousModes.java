package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.Debug;
import com.team766.frc2019.commands.LimeDebug;
import com.team766.frc2019.commands.TeleopAuton;
import com.team766.frc2019.commands.AutonRocketTeleop;
import com.team766.frc2019.commands.LimePickupAuto;
import com.team766.frc2019.commands.LimeScoreAuto;
import com.team766.frc2019.commands.TurnAround;
import com.team766.frc2019.commands.DriveAuto1;
import com.team766.frc2019.commands.DriveAuto2;
import com.team766.frc2019.commands.DriveAuto3;
import com.team766.frc2019.commands.DriveAuto4;
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
    @AutonomousCommand(commandClass= DriveAuto1.class) DriveAuto1,
    @AutonomousCommand(commandClass= DriveAuto2.class) DriveAuto2,
    @AutonomousCommand(commandClass= DriveAuto3.class) DriveAuto3,
    @AutonomousCommand(commandClass= DriveAuto4.class) DriveAuto4,
    //debug
    @AutonomousCommand(commandClass= Debug.class) Debug,
    @AutonomousCommand(commandClass= LimeDebug.class) LimeDebug,
    @AutonomousCommand(commandClass= DriveVelocity.class) DriveVelocity,
}
