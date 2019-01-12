package com.team766.frc2019.commands;

import com.team766.frc2019.commands.DriveForDistance;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class DriveStraight extends DriveForDistance {
    //drives straight for 10 feet
    
    protected void subroutine() {
        driveForDistance(10.0);
    }
}
