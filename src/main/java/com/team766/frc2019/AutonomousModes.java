package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.Debug;
import com.team766.frc2019.commands.DriveAuto1;
import com.team766.frc2019.commands.DriveAuto2;
import com.team766.frc2019.commands.DriveAuto3;
import com.team766.frc2019.commands.DriveAuto4;

public enum AutonomousModes {
    @AutonomousCommand(commandClass= com.team766.frc2019.commands.TeleopAuton.class) TeleopAuton,
    @AutonomousCommand(commandClass= com.team766.frc2019.commands.LimeDrive.class) LimeDrive,
    @AutonomousCommand(commandClass= com.team766.frc2019.commands.AutonRocketTeleop.class) AutonRocketTeleop,
    @AutonomousCommand(commandClass= DriveAuto4.class) DriveAuto4,
    @AutonomousCommand(commandClass= DriveAuto1.class) DriveAuto1,
    @AutonomousCommand(commandClass= DriveAuto2.class) DriveAuto2,
    @AutonomousCommand(commandClass= DriveAuto3.class) DriveAuto3,
    @AutonomousCommand(commandClass= Debug.class) Debug,

}
