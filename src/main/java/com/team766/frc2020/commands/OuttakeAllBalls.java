package com.team766.frc2020.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;


public class OuttakeAllBalls extends Subroutine {
    protected void subroutine(){
        for (int i = 0; i < 5; i++) {
        Robot.outtake.setOuttakePower(1);
        //Robot.outtake.shootingSpeed(1);
        }
    }
}