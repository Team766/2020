package com.team766.frc2019.commands;

import com.team766.frc2019.Robot;

public class AutonElevator extends CalibrateElevator {

    public AutonElevator() {
        super();
    }


    protected void subroutine() {
        super.subroutine();
        super.subroutine();
        super.subroutine();


        System.out.println( "Calibrate done");
        Robot.elevator.setCombinedPosition(Robot.elevator.LVL2);
        waitForSeconds(5.0);
        Robot.elevator.setCombinedPosition(Robot.elevator.LVL3);
        waitForSeconds(5.0);
        Robot.elevator.setCombinedPosition(Robot.elevator.LVL2);
        waitForSeconds(5.0);
        Robot.elevator.setCombinedPosition(Robot.elevator.LVL1);
        //Robot.elevator.setCombinedPosition(Robot.elevator.LVL2);

        


    }


}