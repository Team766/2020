package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.RobotProvider;

public class DrivePrecise extends Subroutine {
    final double TIME; 
    final double POWER;
    final double P = 0.005;
    final double ERROR_MGN = 0.0;

    public DrivePrecise(double time, double power) {
        takeControl(Robot.drive);
        TIME = time;
        POWER = power;

    } 
    
    protected void  subroutine() {
        double m_lPower = POWER;
        double m_rPower = POWER;
        Robot.drive.setDrivePower(m_lPower,m_rPower);
        Robot.drive.gyroReset();
        double m_initial = Robot.drive.getGyroAngle();
        final double start = RobotProvider.instance.getClock().getTime();
        while (RobotProvider.instance.getClock().getTime() - start < TIME) {

            double error = Robot.drive.getGyroAngle() - m_initial;
            double errorP = error * P;
            System.out.println("Cur Error:" + error);
            
            if (Math.abs(error) > ERROR_MGN) { //if the error is not acceptable 
                if (error > 0) { // if drifting left
                    if (m_lPower + errorP < POWER)  { //if the lagging motor can be sped up, do so, hopefully preventing rbt from stopping
                        m_lPower += errorP;
                    } else { //if the lagging motor is already at max speed, slow down the other one
                        m_rPower -= errorP;
                    }
                    
                } else { //drifiting right, do the same but flipped
                    if (m_rPower + errorP < POWER || (m_lPower - errorP <= 0.0 && m_rPower + errorP < POWER)) { 
                        m_rPower += errorP;
                    } else {
                        m_lPower -= errorP;
                    }
                }
            System.out.println("L: " + m_lPower + " R: " + m_rPower);
            Robot.drive.setDrivePower(m_lPower, m_rPower);
            }
            yield();
        }
        Robot.drive.setDrivePower(0.0,0.0);
    }

}


