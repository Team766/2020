package com.team766.frc2020.commands;

import com.team766.controllers.PIDController;
import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.MotionProfile.Profile;
import com.team766.frc2020.MotionProfile.main;
import com.team766.frc2020.mechanisms.WaterWheel;
import com.team766.hal.RobotProvider;
import com.team766.frc2020.MotionProfile.TrapProfile;


public class NextBall extends Subroutine {
    public NextBall() {
        //takeControl(Robot.drive);

    }

    protected void subroutine() {

        double v_max = 10;
        double accel = 10;
        double dist = 10;
        double currentTime = 0;
        double start_velocity = 0;

        double vError;

        WaterWheel waterWheel = new WaterWheel();

        PIDController m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        m_turnController.setSetpoint(0.0);


        Profile motionProfile = new TrapProfile(v_max, accel, dist, 0, 0);

        // while(Robot.waterWheel.getWheelPosition() != motionProfile.getDistAtTime(motionProfile.getFinalTime())) {
        //     currentTime = (Math.abs(waterWheel.getWheelVelocity()) - Math.abs(start_velocity)) / accel;
        //     vError = waterWheel.getWheelVelocity() - motionProfile.getVelocityAtTime(currentTime);
            
        //     m_turnController.calculate(vError, true);
        //     double velocityDifference = m_turnController.getOutput();

        //     System.out.println("Wheel velocity: " + waterWheel.getWheelVelocity() + " Velocity difference: " + velocityDifference); 
        //     waterWheel.setWheelVelocity(motionProfile.getVelocityAtTime(currentTime) + velocityDifference);
            

        // }

        


    }

}