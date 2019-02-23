package com.team766.frc2019;

import com.team766.framework.AutonomousCommand;
import com.team766.frc2019.commands.DriveSquare;
import com.team766.frc2019.commands.ExtendGripper;
import com.team766.frc2019.commands.RetractGripper;
import com.team766.frc2019.commands.Debug;
import com.team766.frc2019.commands.DriveAuto1;
import com.team766.frc2019.commands.DriveAuto2;
import com.team766.frc2019.commands.DriveAuto3;
import com.team766.frc2019.commands.AutonElevator;

public enum AutonomousModes {
    @AutonomousCommand(commandClass= DriveSquare.class) DriveSquare,
    @AutonomousCommand(commandClass= ExtendGripper.class) ExtendGripper,
    @AutonomousCommand(commandClass= RetractGripper.class) RetractGripper,
    @AutonomousCommand(commandClass= Debug.class) Debug,
    @AutonomousCommand(commandClass= DriveAuto1.class) DriveAuto1,
    @AutonomousCommand(commandClass= DriveAuto2.class) DriveAuto2,
    @AutonomousCommand(commandClass= DriveAuto3.class) DriveAuto3,
    @AutonomousCommand(commandClass= AutonElevator.class) AutonElevator,

}
