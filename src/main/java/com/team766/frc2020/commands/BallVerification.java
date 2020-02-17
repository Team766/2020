package com.team766.frc2019.commands;
import com.team766.frc2019.Robot;
import com.team766.frc2019.mechanisms.Ultrasonic;
import com.team766.framework.Subroutine;


public class BallVerification extends Subroutine {

    public BallVerification() {
        takeControl(Robot.ultrasonic);
    }

    public void subroutine(){
        double distance = Robot.ultrasonic.ultraSense();
        System.out.println(distance);
    }


}


