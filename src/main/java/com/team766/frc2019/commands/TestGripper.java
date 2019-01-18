package com.team766.frc2019.commands;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class TestGripper extends Subroutine {
    public TestGripper(){
        takeControl(Robot.flowerGripper);
    }

    @Override
    protected void subroutine() {
        Robot.flowerGripper.extend();
        waitForSeconds(3.0);
        Robot.flowerGripper.retract();

    }
}
