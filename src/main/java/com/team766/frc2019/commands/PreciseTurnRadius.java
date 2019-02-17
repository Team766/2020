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
    double moveDir = 1;
    boolean m_turnDirection;
    //true is left encoder false is right encoder
    //opposite direction of turn (true is right turn false is left turn)

    /**
     * Drives the robot on an arc according to the given angle and radius.
     */
    public PreciseTurnRadius(double targetAngle, double radius, double targetPower, double endPower) {
        m_initialAngle = Robot.drive.getGyroAngle();
        m_targetAngle = targetAngle;
        m_angleDiff = Robot.drive.AngleDifference(m_initialAngle, m_targetAngle);
        if (m_angleDiff > 0) {
            m_turnDirection = true;
        } else {
            m_turnDirection = false;
        }
        if (targetPower < 0) {
            moveDir = -1;
        }
        m_arcLength = 2 * Math.PI * radius * (Math.abs(m_angleDiff) / 360);
        m_insideArcLength = 2 * Math.PI * (radius - (Robot.drive.robotWidth / 2.0)) * (Math.abs(m_angleDiff) / 360);
        m_outsideArcLength = 2 * Math.PI * (radius + (Robot.drive.robotWidth / 2.0)) * (Math.abs(m_angleDiff) / 360);
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD);
        m_targetPower = targetPower;
        m_endPower = endPower;
        
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        double arcPercent = 0;
        double error = 0;
        double currentDistance = 0;

        double turnAdjust = 0.0;
        double leftAdjust = 0.0;
        double rightAdjust = 0.0;
        double straightPower = 0.0;

        double index = 0;

        m_turnController.setSetpoint(0.0);
        currentDistance = Robot.drive.getOutsideEncoderDistance(m_turnDirection) * Robot.drive.DIST_PER_PULSE;
        System.out.println("I'm turning from: " + m_initialAngle + " to: " + m_targetAngle + " and the difference is : " + Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle) + " with an outside arc length of: " + m_outsideArcLength);
        while(currentDistance * moveDir < Math.abs(m_outsideArcLength)) {
            currentDistance = Robot.drive.getOutsideEncoderDistance(m_turnDirection) * Robot.drive.DIST_PER_PULSE;
            
            arcPercent = currentDistance *moveDir / m_outsideArcLength;
            error = Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), (m_initialAngle + (m_angleDiff * arcPercent)));
            m_turnController.calculate(error, true);

            turnAdjust = m_turnController.getOutput();
            straightPower = calcPower();
            if (index++ % 1 == 0) {
                //System.out.println("AngDif: " + roundOff(m_angleDiff, 2) + "   ArcPrc: " + roundOff(arcPercent, 2) + "   Err: " + roundOff(error, 2) + "   ta: " + roundOff(turnAdjust, 2) + "   sp: " + roundOff(straightPower, 2) + " td: " + Math.abs(roundOff(m_outsideArcLength, 2)) + " cd: " + roundOff(currentDistance, 2) * moveDir + " turn dir: " + m_turnDirection + " ca: " + roundOff(Robot.drive.getGyroAngle(), 2) + "   CurTar: " + roundOff(m_initialAngle + (m_angleDiff * arcPercent), 2));
                System.out.println("Speed: " + straightPower + " Adjusted Speed: " + straightPower * (m_insideArcLength / m_outsideArcLength));
            }
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
            index++;
        }
        Robot.drive.setDrive(m_endPower * Robot.drive.POSITION_PER_INCH, m_endPower * Robot.drive.POSITION_PER_INCH, ControlMode.Velocity);
        Robot.drive.resetEncoders();
        yield();
    }

    public double calcPower() {
        double endPower = (Math.abs(m_outsideArcLength) - Math.abs(Robot.drive.getOutsideEncoderDistance(m_turnDirection) * Robot.drive.DIST_PER_PULSE)) * POWER_RAMP;
        return Math.max(Math.min(Math.abs(endPower), Math.abs(m_targetPower)), MIN_POWER) * moveDir;
    }

    public double roundOff(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(value * multiplier) / multiplier;
    }
}