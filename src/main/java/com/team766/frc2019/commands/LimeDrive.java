package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.mechanisms.Drive;
import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.controllers.PIDController;

public class LimeDrive extends Subroutine {

    PIDController m_turnController;
    double m_targetPower;
    double m_endPower;
    double m_endDistance;
    double MIN_POWER = 0.25;
    double POWER_RAMP = 1.0;
    double END_POWER_PERCENT = 0.75;
    double yError;
    double currentX;
    double turnAdjust;
    double straightPower;
    double pOut;

    /**
     * Precisely drives for the set parameters.
     * @param targetPower
     * @param endPower
     */
    
    public LimeDrive() {
        m_turnController = new PIDController(Drive.P,Drive.I, Drive.D, Drive.THRESHOLD);
    }

    protected void subroutine() {
        m_turnController.reset();
        Robot.drive.resetEncoders();
        double index = 0;
        currentX = Robot.limeLight.tx();
        yError = Robot.limeLight.ty();
        System.out.print("y error = " + yError);
        m_turnController.setSetpoint(0.0);
        while ( Math.abs(currentX) < 20 ) {
            currentX = Robot.limeLight.tx();
            yError = Robot.limeLight.ty();
            //System.out.print("y error = " + yError + " x error " + currentX);
            straightPower = 1.0 * Math.pow(Math.E, -0.19*Math.abs(currentX));
            m_turnController.calculate( yError, true);
            pOut = m_turnController.getOutput();
            System.out.println(pOut);
            if (pOut != Double.NaN) {
                turnAdjust = pOut;
            }
            System.out.println("straight power: " + straightPower + " turn adjust: " + turnAdjust + "y error = " + yError + " x error " + currentX);
            if (turnAdjust > 0) {
                Robot.drive.setDrive(straightPower - turnAdjust, straightPower, ControlMode.PercentOutput);
            } else {
               Robot.drive.setDrive(straightPower, straightPower - turnAdjust, ControlMode.PercentOutput);
            }
        }

    }








/*
        double distanceToTarget = Robot.limeLight.distanceToTarget(1, Robot.limeLight.tx());
        double targetAngle = Robot.limeLight.ty();
        m_turnController.setSetpoint(0.0);
        System.out.println("I shall seek and destroy/put on a hatch on something");
        while((distanceToTarget > m_endDistance) && !Robot.m_oi.driverControl) {
            distanceToTarget = Robot.limeLight.distanceToTarget(1, targetAngle);
            m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), Robot.limeLight.ty()), true);
            double turnPower = m_turnController.getOutput();
            double straightPower = calcPower(Math.abs(getCurrentDistance() / distanceToTarget));
            if (turnPower > 0) {
                Robot.drive.setDrive(straightPower - turnPower, straightPower, ControlMode.PercentOutput);
            } else {
                Robot.drive.setDrive(straightPower, straightPower + turnPower, ControlMode.PercentOutput);
            }
            if (index % 15 == 0 && Robot.drive.isEnabled()) {
                System.out.println("TA: " + Robot.limeLight.ty() + " CA: " + Robot.drive.getGyroAngle() + " Diff: " + Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), Robot.limeLight.ty()) + " Pout: " + m_turnController.getOutput() + " Power: " + calcPower(Math.abs(getCurrentDistance() / distanceToTarget)) + " Dist to Target: " + distanceToTarget + " Left Power: " + Robot.drive.leftMotorVelocity() + " Right Power: " + Robot.drive.rightMotorVelocity());
            }
            index++;
            if (!Robot.drive.isEnabled()){
                Robot.drive.nukeRobot();
                m_turnController.reset();
                return;
            }
        }
        System.out.println("PreciseDrive finished");
        Robot.drive.setDrive(m_endPower, m_endPower, ControlMode.PercentOutput);
        Robot.drive.resetEncoders();
        yield();
        return;
    }
    */

    public double getCurrentDistance() {
        return(((Robot.drive.rightEncoderDistance() + Robot.drive.leftEncoderDistance())*Robot.drive.DIST_PER_PULSE)/2.0);
    }

    private double calcPower(double arcPercent) {
        if (arcPercent < END_POWER_PERCENT) {
            return m_targetPower;
        }
        double scaledPower = (1 - (arcPercent - END_POWER_PERCENT) / (1 - END_POWER_PERCENT)) * m_targetPower;
        if (m_targetPower >= 0) {
            return Math.max(MIN_POWER, scaledPower);
        } else {
            return Math.min(-MIN_POWER, scaledPower);
        }
    }

}