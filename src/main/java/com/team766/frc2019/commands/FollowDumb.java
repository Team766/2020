package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.RobotProvider;

public class FollowDumb extends Subroutine {
    public FollowDumb() {
        takeControl(Robot.drive);
    }
    protected void subroutine() {
        LimeLogger e = new LimeLogger();
        /*
        double distance = LimeLight.distanceDumb();
        while (distance > 30) {
            e.center2();
            distance = LimeLight.distanceDumb();
            yield();
        }*/

        e.backup(0.001);
    }

}


