package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Date;

import com.team766.controllers.PIDController;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;

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
    double MIN_POWER = 0.25;
    double POWER_RAMP = 1.0;
    double END_POWER_PERCENT = 0.75;
    double moveDir = 1;
    boolean m_turnDirection;
    private JoystickReader m_joystick1  = RobotProvider.instance.getJoystick(1);

    //true is left encoder false is right encoder
    //opposite direction of turn (true is right turn false is left turn)

    /**
     * Drives the robot on an arc according to the given angle and radius.
     */
    public PreciseTurnRadius(double targetAngle, double radius, double targetPower, double endPower) {
        

        
    }

    protected void subroutine() {
        Robot.drive.resetEncoders();
        double arcPercent = 0;
        double error = 0;
        double currentDistance = 0;

        double turnAdjust = 0.0;
        double leftAdjust = 0.0;
        double rightAdjust = 0.0;
        double straightPower = 0.0;
        double leftPower = 0.0;
        double rightPower = 0.0;

        double index = 0;

        m_turnController.reset();
        m_turnController.setSetpoint(0.0);
        currentDistance = Robot.drive.getOutsideEncoderDistance(m_turnDirection) * Robot.drive.DIST_PER_PULSE;
        System.out.println("I'm turning from: " + m_initialAngle + " to: " + m_targetAngle + " and the difference is : " + m_angleDiff + " with an outside arc length of: " + m_outsideArcLength + " Current Dist: " + currentDistance);
        while((currentDistance * moveDir < Math.abs(m_outsideArcLength) && (Math.abs(m_joystick1.getRawAxis(1)) < .2))) {
            SmartDashboard.putNumber("Current angle", Robot.drive.getGyroAngle());
            currentDistance = Robot.drive.getOutsideEncoderDistance(m_turnDirection) * Robot.drive.DIST_PER_PULSE;
            
            arcPercent = (currentDistance * moveDir) / m_outsideArcLength;
            error = Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), (m_initialAngle + (m_angleDiff * arcPercent)));
            m_turnController.calculate(error, true);

            turnAdjust = m_turnController.getOutput();
            straightPower = calcPower(arcPercent);
            
            if (moveDir > 0) {
                if (turnAdjust < 0) {
                    leftAdjust = 0;
                    rightAdjust = turnAdjust;
                } else {
                    leftAdjust = -turnAdjust;
                    rightAdjust = 0;
                }
            } else {
                if (turnAdjust < 0) {
                    leftAdjust = -turnAdjust;
                    rightAdjust = 0;
                } else {
                    leftAdjust = 0;
                    rightAdjust = turnAdjust;
                }
            }
            if (m_turnDirection) {
                leftPower = (straightPower + leftAdjust);
                rightPower = absoluteMin((straightPower * (m_insideArcLength / m_outsideArcLength)) + rightAdjust, straightPower);
            } else {
                leftPower = absoluteMin((straightPower * (m_insideArcLength / m_outsideArcLength)) + leftAdjust, straightPower);
                rightPower = (straightPower + rightAdjust);
            }
            Robot.drive.setDrive(leftPower, rightPower);
            //if (index % 15 == 0) {
                //System.out.println("AngDif: " + roundOff(m_angleDiff, 2) + "   ArcPrc: " + roundOff(arcPercent, 2) + "   Err: " + roundOff(error, 2) + "   ta: " + roundOff(turnAdjust, 2) + " sp: " + roundOff(straightPower, 2) + " td: " + roundOff(m_outsideArcLength, 2) + " cd: " + roundOff(currentDistance, 2) + " turn dir: " + m_turnDirection + " ca: " + roundOff(Robot.drive.getGyroAngle(), 2) + "   CurTar: " + roundOff(m_initialAngle + (m_angleDiff * arcPercent), 2) + " Left: " + roundOff(leftPower, 4) + " Right: " +  roundOff(rightPower, 4) + " MoveDir: " + moveDir);
                //System.out.println(m_turnController);
            //}
            SmartDashboard.putNumber("Angle Difference", m_angleDiff);
            SmartDashboard.putNumber("Arc Percent", arcPercent);
            SmartDashboard.putNumber("Current Error", error);
            SmartDashboard.putNumber("Current Angle", Robot.drive.getGyroAngle());
            SmartDashboard.putNumber("Current Target", m_initialAngle + (m_angleDiff * arcPercent));
            SmartDashboard.putNumber("Turn Adjust", turnAdjust);
            SmartDashboard.putNumber("Left Power", leftPower);
            SmartDashboard.putNumber("Right Power", rightPower);
            SmartDashboard.putNumber("Current Distance", currentDistance);
            SmartDashboard.putNumber("Target Distance", m_outsideArcLength);
            SmartDashboard.putBoolean("Turn Direction", m_turnDirection);
            SmartDashboard.putNumber("Move Direction", moveDir);


            index++;
            if (!Robot.drive.isEnabled()) {
                Robot.drive.nukeRobot();
                m_turnController.reset();
                yield();
                return;
            }
        }
        if (!(Math.abs(m_joystick1.getRawAxis(1)) < .2)) {
            callSubroutine(new TeleopAuton());
        }
        System.out.println("PreciseTurnRadius loop done");
        Robot.drive.setDrive(m_endPower, m_endPower);
        Robot.drive.resetEncoders();
        yield();
        return;
    }

    private double calcPower(double arcPercent) {
        //double endPower = (((m_endPower - m_targetPower) / (1 - arcPercent)) * (arcPercent - END_POWER_PERCENT)) + m_targetPower;
        //return Math.max(Math.min(Math.abs(endPower), Math.abs(m_targetPower)), MIN_POWER) * moveDir;

        if (arcPercent < END_POWER_PERCENT) {
            return m_targetPower;
        }
        double scaledPower = (1 - (arcPercent - END_POWER_PERCENT) / (1 - END_POWER_PERCENT)) * m_targetPower;
        if (m_targetPower >= 0) {
            return Math.max(MIN_POWER, scaledPower);
        } else {
            return Math.min(-MIN_POWER, scaledPower);
        }
        //return m_targetPower;
    }

    private double roundOff(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(value * multiplier) / multiplier;
    }

    private double absoluteMin(double value, double initial) {
        if (initial > 0) {
            return Math.max(value, -MIN_POWER);
        } else {
            return Math.min(value, MIN_POWER);
        }
    }
}