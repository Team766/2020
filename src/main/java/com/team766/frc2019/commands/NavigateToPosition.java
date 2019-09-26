package com.team766.frc2019.commands;

import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.RobotProvider;
import java.util.Date;

public class NavigateToPosition extends Subroutine {
    public NavigateToPosition() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        // basically, take a point and go to it
    }  
}