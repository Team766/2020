package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.RobotProvider;

public class DriveSquare extends Subroutine {
    public DriveSquare() {
        takeControl(Robot.drive);
    }
    protected void  subroutine() {
        //callSubroutine(new DrivePrecise(5.0, 0.5));
        //callSubroutine(new TurnPrecise2(90,0.2));    
        //System.out.println(LimeLight.distanceDumb(0, 6, 12));
        //callSubroutine(new LimeLight());
        final double start = RobotProvider.instance.getClock().getTime();
        LimeLight.setPipeline(1);
        while (RobotProvider.instance.getClock().getTime() - start < 60.0) {
            System.out.println(LimeLight.distanceDumb());
            yield();
        }

    }

}


