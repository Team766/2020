package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.controllers.PIDController;

public class PreciseTurnRadius extends Subroutine {

    PIDController m_turnController;
    double m_targetAngle;
    double m_angleDiff;
    double m_targetPower;
    double m_startPower;
    double m_endPower;
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

    /**
     * Drives the robot on an arc according to the given angle and radius.
     */
    public PreciseTurnRadius(double targetAngle, double radius, double targetPower, double startPower, double endPower) {
        m_initialAngle = Robot.drive.getGyroAngle();
        m_targetAngle = targetAngle;
        m_angleDiff = Robot.drive.AngleDifference(m_initialAngle, m_targetAngle);
        if (m_angleDiff < 0) { m_turnDirection = false; } else { m_turnDirection = true; }
        m_arcLength = 2 * Math.PI * radius * (m_angleDiff / 360);
        m_insideArcLength = 2 * Math.PI * (radius - (Robot.drive.robotWidth / 2.0)) * (m_angleDiff / 360);
        m_outsideArcLength = 2 * Math.PI * (radius + (Robot.drive.robotWidth / 2.0)) * (m_angleDiff / 360);
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
        m_targetPower = targetPower;
        m_startPower = startPower;
        m_endPower = endPower;
        
        //takeControl(Robot.drive);
    }

    protected void subroutine() {
        double arcPercent = 0;
        double error = 0;

        double turnAdjust = 0.0;
        double leftAdjust = 0.0;
        double rightAdjust = 0.0;
        double straightPower = 0.0;

        m_turnController.setSetpoint(0.0);
        while((Robot.drive.getOutsideEncoderDistance(m_turnDirection) * Robot.drive.DIST_PER_PULSE) < m_outsideArcLength) {

            arcPercent = (Robot.drive.getOutsideEncoderDistance(m_turnDirection) * Robot.drive.DIST_PER_PULSE) / m_outsideArcLength;
            error = Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), (m_initialAngle + (m_angleDiff * arcPercent)));
            m_turnController.calculate(error, true);

            turnAdjust = m_turnController.getOutput();
            straightPower = calcPower();
            System.out.println("AngDif: " + m_angleDiff + "   ArcPrc: " + arcPercent + "   Err: " + error + "   CurTar: " + (m_initialAngle + (m_angleDiff * arcPercent)) + "   ta: " + turnAdjust + "   sp: " + straightPower + " td: " + m_outsideArcLength + " cd: " + (Robot.drive.getOutsideEncoderDistance(m_turnDirection) * Robot.drive.DIST_PER_PULSE) + " turn dir: " + (m_turnDirection) + " ca: " + Robot.drive.getGyroAngle());
            /*if (turnAdjust > 0) {
                leftAdjust = turnAdjust;
                rightAdjust = 0;
            } else {
                leftAdjust = 0;
                rightAdjust = -turnAdjust;
            }*/
            if (m_turnDirection) {
                Robot.drive.setDrive((straightPower + leftAdjust) * Robot.drive.POSITION_PER_INCH, ((straightPower * (m_insideArcLength / m_outsideArcLength)) + rightAdjust) * Robot.drive.POSITION_PER_INCH, ControlMode.Velocity);
            } else {
                Robot.drive.setDrive(((straightPower * (m_insideArcLength / m_outsideArcLength)) + leftAdjust) * Robot.drive.POSITION_PER_INCH, (straightPower + rightAdjust) * Robot.drive.POSITION_PER_INCH, ControlMode.Velocity);
            }
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
        return Math.max(Math.min(Math.min(startPower, endPower), m_targetPower), MIN_POWER);

    }
}