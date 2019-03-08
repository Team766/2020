package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.DriveSquare;
import com.team766.frc2019.commands.ExtendGripper;
import com.team766.frc2019.commands.RetractGripper;
import com.team766.frc2019.commands.Debug;
import com.team766.frc2019.commands.DriveAuto1;
import com.team766.frc2019.commands.DriveAuto2;
import com.team766.frc2019.commands.DriveAuto3;
import com.team766.frc2019.commands.DriveAuto4;
import com.team766.frc2019.commands.DriveAuto5;
import com.team766.frc2019.commands.DriveAuto6;
import com.team766.frc2019.commands.DriveAuto7;
import com.team766.frc2019.commands.TurnOnce;

public enum AutonomousModes {
    @AutonomousCommand(commandClass= com.team766.frc2019.commands.TeleopAuton.class) TeleopAuton,
    @AutonomousCommand(commandClass= com.team766.frc2019.commands.StraightTeleopAuton.class) StraightTeleopAuton,
  //  @AutonomousCommand(commandClass= DriveAuto4.class) DriveAuto4,
  //  @AutonomousCommand(commandClass= DriveAuto1.class) DriveAuto1,
  //  @AutonomousCommand(commandClass= DriveAuto2.class) DriveAuto2,
  //  @AutonomousCommand(commandClass= DriveAuto3.class) DriveAuto3,
  //  @AutonomousCommand(commandClass= DriveAuto5.class) DriveAuto5,
  //  @AutonomousCommand(commandClass= DriveAuto6.class) DriveAuto6,
    @AutonomousCommand(commandClass= DriveAuto7.class) DriveAuto7,
    @AutonomousCommand(commandClass= Debug.class) Debug,

}
