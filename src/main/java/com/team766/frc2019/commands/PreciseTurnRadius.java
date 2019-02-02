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
    private EncoderReader outsideEncoder;

    public PreciseTurnRadius(double targetAngle, double radius, double targetPower, double startPower, double endPower) {
        double difference = Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), targetAngle);
        if (difference < 0) { m_turnDirection = false; } else { m_turnDirection = true; }
        m_arcLength = (2 * Math.PI * radius * (difference / 360));
        m_insideArcLength = (2 * Math.PI * (radius - (Robot.drive.robotWidth / 2.0)) * (difference / 360));
        m_outsideArcLength = (2 * Math.PI * (radius + (Robot.drive.robotWidth / 2.0)) * (difference / 360));
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
        m_targetAngle = targetAngle;
        m_targetPower = targetPower;
        m_startPower = startPower;
        m_endPower = endPower;
        m_initialAngle = Robot.drive.getGyroAngle();
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double angleDiff = Robot.drive.AngleDifference(m_initialAngle, m_targetAngle);
        double arcPercent = 0;
        double error = 0;
        m_turnController.setSetpoint(0.0);
        while((Robot.drive.getOutsideEncoder(m_turnDirection).getDistance() * Robot.drive.DIST_PER_PULSE) < m_outsideArcLength) {

            arcPercent = Robot.drive.getOutsideEncoder(m_turnDirection).getDistance() / m_outsideArcLength;
            error = Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), (m_initialAngle + (angleDiff * arcPercent)));
            m_turnController.calculate(error, true);
            System.out.println("AngleDiff: " + angleDiff + " ArcPercent: " + arcPercent);
            //System.out.println(" CurrDist: " + (angleDiff * arcPercent) + " Exp: " + (m_initialAngle + (angleDiff * arcPercent)) + " CurrAng: " + Robot.drive.getGyroAngle() + " Error: " + error + " Pout: " + m_turnController.getOutput());
            
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
                Robot.drive.setDrivePower(straightPower + leftAdjust, (straightPower * (m_insideArcLength / m_outsideArcLength)) + rightAdjust);
            } else {
                Robot.drive.setDrivePower((straightPower * (m_insideArcLength / m_outsideArcLength)) + leftAdjust, straightPower + rightAdjust);
            }
            //System.out.println("currdist: " + getCurrentDistance() + " power: " + straightPower + " ladj: " + leftAdjust + " radj: " + rightAdjust);
            //System.out.println("arc: " + m_arcLength + " out: " + m_outsideArcLength + " in: " + m_insideArcLength);
            yield();
        }
        //Robot.drive.setDrivePower(m_endPower, m_endPower);
        Robot.drive.resetEncoders();
    }

    public double getCurrentDistance() {
        return(((Robot.drive.rightEncoderDistance() + Robot.drive.leftEncoderDistance())*Robot.drive.DIST_PER_PULSE)/2.0);
    }

    public double getBearingError() {
        return Robot.drive.getGyroAngle() - (m_initialAngle - (Robot.drive.AngleDifference(m_targetAngle, m_initialAngle) * (getCurrentDistance() / m_arcLength)));
    }

    public double calcPower() {
        double currentDistance = getCurrentDistance();  
        
        double startPower = currentDistance * POWER_RAMP;
        double endPower = (m_arcLength - currentDistance) * POWER_RAMP;
        return Math.min(Math.min(startPower, endPower), m_targetPower) + MIN_POWER;

    }
}