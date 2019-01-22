package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.RobotProvider;
import java.util.Date;

public class SimpleTurn extends Subroutine {
    public SimpleTurn() {
        takeControl(Robot.drive);
    }
    protected void subroutine() {
        Robot.drive.resetGyro();
        Robot.drive.setDrivePower(0.75, -0.75);
        double startTime = new Date().getTime();
        while((new Date().getTime() - startTime) < 2000) {
            System.out.println(Robot.drive.getGyroAngle() + ", " + new Date().getTime());
            yield();
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }  
}