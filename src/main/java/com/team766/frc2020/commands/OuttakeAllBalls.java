package com.team766.frc2020.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;


public class OuttakeAllBalls extends Subroutine {
    public OuttakeBall() {
    protected void subroutine(){
        for (int i = 0; i < 5; i++);
        Robot.outtake.setOuttakeState(true);
        Robot.outtake.shootingSpeed(1);
        
    }
}