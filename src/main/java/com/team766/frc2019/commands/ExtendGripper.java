package com.team766.frc2019.commands;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class ExtendGripper extends Subroutine {
    public ExtendGripper(){
        takeControl(Robot.flowerGripper);
    }

   
    @Override
    protected void subroutine() {
        Robot.flowerGripper.extend();
    }
}
