package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import java.util.*;

public class TurnPrecise extends Subroutine {
    int m_degrees;
    double m_speed;
     
    public TurnPrecise(int degrees, double speed) {
        m_degrees = degrees;
        m_speed = speed / 2.0;
        takeControl(Robot.drive);
    } 
    protected void subroutine() {
        Robot.drive.gyroReset();
        if (m_degrees > 0) {
            Robot.drive.setDrivePower(m_speed,-m_speed);
            /*waitFor(() -> Robot.drive.getGyroAngle() >= m_degrees);
                if (Robot.drive.getGyroAngle() - 15  m_degrees - 15) {
                    Robot.drive.setDrivePower(-m_speed/2.0,m_speed/2.0);
                }
            */
            boolean inertial1 = false; //has reduced the speed 
            boolean inertial2 = false;
            while (Robot.drive.getGyroAngle() < m_degrees) {
                double angle = Robot.drive.getGyroAngle();
                if (angle > 15 && !inertial1) {
                    m_speed = m_speed * 2;
                    Robot.drive.setDrivePower(m_speed,-m_speed);
                    inertial1 = true;   
                }
                if (angle > m_degrees - 15 && !inertial2) {
                    m_speed = m_speed / 2;
                    Robot.drive.setDrivePower(m_speed, -m_speed);
                    inertial2 = true;
                }
                yield();
            }
            Robot.drive.setDrivePower(0.0,0.0);
        } else {
            Robot.drive.setDrivePower(-0.5,0.5);
            //waitFor(() -> Robot.drive.getGyroAngle() <= m_degrees);
            while (Robot.drive.getGyroAngle() >= m_degrees) {
                System.out.println(Robot.drive.getGyroAngle());
                yield();
            }
            Robot.drive.setDrivePower(0.0,0.0);
        }
        
    }

}