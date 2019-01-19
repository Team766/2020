package com.team766.frc2019.commands;

import com.team766.controllers.PIDController;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class PreciseTurn extends Subroutine {
    private double m_angle = 90;
    private PIDController m_PID;
    private static double P = 10;
    private static double I = 2;
    private static double D = 20;
    private static double THRESHOLD = 0.2;

    public PreciseTurn(double angle) {
        m_angle = angle;
        Robot.drive.resetGyro();
        m_PID = new PIDController(P, I, D, THRESHOLD);
        m_PID.setSetpoint(angle);
        m_PID.setMaxoutputHigh(0.5);      
        m_PID.setMaxoutputLow(0.1);  
        
            takeControl(Robot.drive);

    }        
 
    protected void subroutine() {
        Robot.drive.startTurn(m_angle);
       
       int loops = 0;
        while (!Robot.drive.isTurnDone() && loops < 100) {
            yield();
            loops += 1;
        }

        Robot.drive.setDrivePower(0.0, 0.0);
    }
}

    /*protected void subroutine_old() {
        double leftDrivePower;
        double rightDrivePower;
        double speedForce = 0.25;
        if (m_angle > 0) {
            leftDrivePower = 1.0;
            rightDrivePower = -1.0;
        } else {
            leftDrivePower = -1.0;
            rightDrivePower = 1.0;
        }
        Robot.drive.setDrivePower(leftDrivePower * speedForce, rightDrivePower * speedForce);

       
        while (Math.abs(Robot.drive.getGyroAngle()) < Math.abs(m_angle)) {
            
            double currentAngle = Math.abs(Robot.drive.getGyroAngle());
            if (currentAngle < RAMP_ANGLE) {
                speedForce = currentAngle/ RAMP_ANGLE;
                if (speedForce < .25)
                    speedForce = .25;
            } else if (currentAngle < m_angle-RAMP_ANGLE) { 
                speedForce = 1;
            } else {
                speedForce = 1 - (currentAngle - (m_angle-RAMP_ANGLE))/RAMP_ANGLE;
                if (speedForce < .25)
                    speedForce = .25;
            } 
            Robot.drive.setDrivePower(leftDrivePower * speedForce, rightDrivePower * speedForce);
            yield();
        }

        Robot.drive.setDrivePower(0.0, 0.0);
        
    }
 
} 
*/
