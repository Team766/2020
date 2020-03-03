package com.team766.frc2020.commands;

import com.team766.controllers.PIDController;
import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.MotionProfile.Profile;
import com.team766.frc2020.MotionProfile.main;
import com.team766.frc2020.mechanisms.WaterWheel;
import com.team766.hal.RobotProvider;
import com.team766.frc2020.MotionProfile.TrapProfile;


public class WheelMotionMagic extends Subroutine {
    public WheelMotionMagic() {
        //takeControl(Robot.drive);
    }

    protected void subroutine() {

        double v_max = 20;
        double accel = 10;
        double dist = 777;
        double currentTime = 0;
        double start_velocity = 0;
        
        Robot.waterwheel.setMotionMagic(dist);



    }

}