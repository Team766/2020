package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import java.util.*;

public class TurnPrecise2 extends Subroutine {
    final int DEGREES;
    double m_speed;
     
    public TurnPrecise2(int degrees, double speed) {
        DEGREES = degrees;
        m_speed = speed / 2.0;
        takeControl(Robot.drive);
    } 
    protected void subroutine() {
        Robot.drive.gyroReset();
        
        double m_lSign = DEGREES > 0 ? 1.0 : -1.0; //if positive, ma
        double m_rSign = m_lSign * -1.0;
        double m_degreesAbs = Math.abs(DEGREES);
        Robot.drive.setDrivePower(m_lSign * m_speed, m_rSign * m_speed);
        boolean inertial1 = false; //has reduced the speed 
        boolean inertial2 = false;
        while (Robot.drive.getGyroAngle() < DEGREES) {
            double angle = Math.abs(Robot.drive.getGyroAngle());
            if (angle > 15 && !inertial1) {
                m_speed = m_speed * 2;
                Robot.drive.setDrivePower(m_lSign * m_speed, m_rSign * m_speed);
                inertial1 = true;   
            }
            if (angle > m_degreesAbs - 15 && !inertial2) {
                m_speed = m_speed / 2;
                Robot.drive.setDrivePower(m_lSign * m_speed, m_rSign * m_speed);
                inertial2 = true;
            }
            System.out.println(angle);
            yield();
        }
        Robot.drive.setDrivePower(0.0,0.0);
    }

}