package com.team766.frc2019.commands;

import com.team766.controllers.PIDController;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import java.util.Date;


public class PreciseTurn extends Subroutine {
    private double m_angle;
    public PreciseTurn(double angle) {
        m_angle = angle;
        //takeControl(Robot.drive);
    }        
 
    protected void subroutine() {
        double startTime = new Date().getTime();

        Robot.drive.startTurn(m_angle);
       
       System.out.println("precise turn subroutine");
        while (!Robot.drive.isTurnDone()) {
            yield();         
        }
        double endTime = new Date().getTime();
        System.out.println("Precise turn is done in " + (endTime - startTime));

 
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
