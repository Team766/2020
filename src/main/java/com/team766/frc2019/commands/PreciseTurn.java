package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.controllers.PIDController;

public class PreciseTurn extends Subroutine {

    double m_turnAngle;
    double m_rampAngle;
    PIDController m_turnController;

    public PreciseTurn(double turnAngle) {
        m_turnAngle = turnAngle;
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double oldAngle = Robot.drive.getGyroAngle() % 360;
        double newAngle = (oldAngle + m_turnAngle) % 360;
        double targetAngle;
        if(Math.abs(newAngle - oldAngle) < Math.abs(newAngle - 360 - oldAngle)) {
			targetAngle = (newAngle - oldAngle) % 360; // No zero cross
        } else {
            targetAngle = (newAngle - 360 - oldAngle) % 360; // Zero cross
        }
        double power = 0;
        System.out.println("hey im gonna turn");
        System.out.println("Current Angle : " + oldAngle + " Target Angle: " + targetAngle + " Check: " + Robot.drive.isTurnDone(m_turnController));
        m_turnController.setSetpoint(targetAngle);
        m_turnController.calculate(Robot.drive.getGyroAngle(), true);
        while(!(Robot.drive.isTurnDone(m_turnController))) {
            m_turnController.calculate(Robot.drive.getGyroAngle(), true);
            power = m_turnController.getOutput();
            if (Math.abs(power) < Robot.drive.MIN_TURN_SPEED) {
                if (power < 0) {
                    power = -Robot.drive.MIN_TURN_SPEED;
                } else {
                    power = Robot.drive.MIN_TURN_SPEED;
                }
            }
            Robot.drive.setDrivePower(power, -power);
            yield();
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}
