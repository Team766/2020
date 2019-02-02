package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.controllers.PIDController;
import com.team766.hal.EncoderReader;

public class PreciseTurnRadius extends Subroutine {

    PIDController m_turnController;
    double m_targetAngle;
    double m_targetPower;
    double m_startPower;
    double m_endPower;
    //double m_adjustment;
    double m_arcLength;
    double m_insideArcLength;
    double m_outsideArcLength;
    double m_insidePower;
    double m_outsidePower;
    double m_initialAngle;
    double MIN_POWER = 0.2;
    double POWER_RAMP = 1.0;
    boolean m_turnDirection;
    //true is right false is left

    public PreciseTurnRadius(double targetAngle, double radius, double targetPower, double startPower, double endPower) {
        double difference = Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), targetAngle);
        m_arcLength = (2 * Math.PI * radius * (difference / 360)) / 12;
        if (difference > 180.0) { m_targetAngle -= 360; } else if (difference < -180.0) { m_targetAngle += 360; }
        if (difference < 0) { m_turnDirection = false; } else { m_turnDirection = true; }
        m_insideArcLength = (2 * Math.PI * (radius - (Robot.drive.robotWidth / 2.0)) * (difference / 360)) / 12;
        m_outsideArcLength = (2 * Math.PI * (radius + (Robot.drive.robotWidth / 2.0)) * (difference / 360)) / 12;
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
        m_targetAngle = targetAngle;
        m_targetPower = targetPower;
        m_startPower = startPower;
        m_endPower = endPower;
        m_outsidePower = targetPower;
        m_insidePower = targetPower * (m_insideArcLength / m_outsideArcLength);
        m_initialAngle = Robot.drive.getGyroAngle();
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        m_turnController.setSetpoint(0.0);
       while((Robot.drive.getOutsideEncoder(m_turnDirection).getDistance() * Robot.drive.DIST_PER_PULSE) < m_outsideArcLength) {
            m_turnController.setSetpoint(Robot.drive.AngleDifference(m_targetAngle, m_initialAngle) * (getCurrentDistance() / m_arcLength));
            m_turnController.calculate(getBearingError(), true);
            double turnAdjust = m_turnController.getOutput();
            double leftAdjust;
            double rightAdjust;
            double straightPower = calcPower();
            if (turnAdjust < 0) {
                leftAdjust = -turnAdjust;
                rightAdjust = 0;
            } else {
                leftAdjust = 0;
                rightAdjust = turnAdjust;
            }
            if (m_turnDirection) {
                Robot.drive.setDrivePower((m_outsidePower * straightPower) + leftAdjust, (m_insidePower * straightPower) + rightAdjust);
            } else {
                Robot.drive.setDrivePower((m_insidePower * straightPower) + leftAdjust, (m_outsidePower * straightPower) + rightAdjust);
            }
            System.out.println("Error: " + getBearingError() + " Current Dist: " + (Robot.drive.getOutsideEncoder(m_turnDirection).getDistance() * Robot.drive.DIST_PER_PULSE) + " Target Dist: " + m_outsideArcLength);
            yield();
        }
        Robot.drive.setDrivePower(m_endPower, m_endPower);
        Robot.drive.resetEncoders();
    }

    public double getCurrentDistance() {
        return(((Robot.drive.rightEncoderDistance() + Robot.drive.leftEncoderDistance())*Robot.drive.DIST_PER_PULSE)/2.0);
    }

    public double getBearingError() {
        return Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), (m_initialAngle + (Robot.drive.AngleDifference(m_targetAngle, m_initialAngle) * (getCurrentDistance() / m_arcLength))));
    }

    public double calcPower() {
        double currentDistance = getCurrentDistance();  
        
        double startPower = currentDistance * POWER_RAMP;
        double endPower = (m_arcLength - currentDistance) * POWER_RAMP;
        return Math.min(Math.min(startPower, endPower), m_targetPower) + MIN_POWER;

    }
}