package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import java.util.*;

public class TurnInertia extends Subroutine {
    private double RAMP_ANGLE = 20/90;
    private double m_angle;
    private double m_left = 1.0; 
    private double m_right = 1.0;
    private double m_power = 1.0;

    public TurnInertia(double angle) {
        m_angle = angle;
    }

    protected void subroutine() {
        final double RAMP_AREA = m_angle * RAMP_ANGLE;
        final double RAMP_AREA_SECOND = m_angle - (m_angle * RAMP_ANGLE);
        while (Robot.drive.getGyroAngle() < m_angle) { //turning left
            double gyroAngle = Robot.drive.getGyroAngle(); 
            if (gyroAngle < (RAMP_AREA) && gyroAngle > 0) {
                m_power = gyroAngle / (RAMP_ANGLE);
            }
            if (gyroAngle >= RAMP_AREA && gyroAngle < RAMP_AREA_SECOND) {
                m_power = 1.0;
            }
            if (gyroAngle >= RAMP_AREA_SECOND && gyroAngle < m_angle) {
                m_power = (gyroAngle - RAMP_AREA_SECOND) / RAMP_AREA;
            }
            
        }
}

}