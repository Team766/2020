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

        double v_max = 20;
        double accel = 10;
        double dist = 777;
        double currentTime = 0;
        double start_velocity = 0;

        double vError;

        WaterWheel waterWheel = new WaterWheel();

        System.out.println("Wheel position initial: " + Robot.waterwheel.getWheelPosition());

        waterWheel.setWheelPosition(820);

        System.out.println("Wheel position final: " + Robot.waterwheel.getWheelPosition());

        // PIDController m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        // m_turnController.setSetpoint(0.0);
        
        // double initialWheelPosition = Robot.waterwheel.getWheelPosition();

        // Profile motionProfile = new TrapProfile(v_max, accel, dist, 1, 0);

        // while(Robot.waterwheel.getWheelPosition() != motionProfile.getDistAtTime(motionProfile.getFinalTime())) {
        //     currentTime = (Math.abs(Robot.waterwheel.getWheelVelocity()) - Math.abs(start_velocity)) / accel;
        //     //System.out.println("current time: " + currentTime + " velocity at current time:" +  motionProfile.getVelocityAtTime(currentTime));
        //     vError = motionProfile.getVelocityAtTime(currentTime) - Robot.waterwheel.getWheelVelocity();
            
        //     m_turnController.calculate(vError, true);
        //     double velocityDifference = m_turnController.getOutput();
        //     System.out.println("wheel difference: " + (Robot.waterwheel.getWheelPosition() - initialWheelPosition) + " distance at final time: " + motionProfile.getDistAtTime(motionProfile.getFinalTime()));
        //     //System.out.println("Wheel velocity: " + Robot.waterwheel.getWheelVelocity() + " Velocity difference: " + velocityDifference); 
        //     Robot.waterwheel.setWheelPower(motionProfile.getVelocityAtTime(currentTime) + velocityDifference);
        
        // }

        


    }

}