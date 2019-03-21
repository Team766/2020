package com.team766.frc2019.commands;

import com.team766.controllers.TimeProviderI;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.mechanisms.Drive;
import com.team766.frc2019.mechanisms.DriveI;
import com.team766.frc2019.mechanisms.LimeLight;
import com.team766.frc2019.mechanisms.LimeLightI;
import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.controllers.PIDController;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;

public class LimePickup extends Subroutine {

    PIDController m_turnController;
    double m_targetPower;
    double m_endPower;
    double m_endDistance;
    double MIN_POWER = 0.25;
    double POWER_RAMP = 1.0;
    double END_POWER_PERCENT = 0.75;
    int index = 0;
    double yError;
    double currentX;
    double turnAdjust;
    double straightPower;
    double pOut;
    DriveI m_drive;
    LimeLightI m_limeLight;
    private JoystickReader m_joystick1  = RobotProvider.instance.getJoystick(1);
    private JoystickReader m_joystick2 = RobotProvider.instance.getJoystick(2);
  

    /**
     * Precisely drives for the set parameters.
     */

    /*
    public LimeDrive() {
        takeControl(Robot.drive);
    }
    */

    public LimePickup(DriveI drive, LimeLightI limeLight, TimeProviderI timeProvider ) {
        m_drive = drive;
        m_limeLight = limeLight;
        m_turnController = new PIDController(LimeLight.P, LimeLight.I, LimeLight.D, LimeLight.THRESHOLD, timeProvider );
    }

    protected void subroutine() {
            turnAdjust = 0;
            m_turnController.reset();
            m_drive.resetEncoders();
            currentX = m_limeLight.tx();
            yError = m_limeLight.ty();
            System.out.print("y error = " + yError);
            m_turnController.setSetpoint(0.0);
            callSubroutine(new ExtendGripper());
            while ( Math.abs(currentX) < 19.9 && (Math.abs(m_joystick1.getRawAxis(1)) < .2 || Math.abs(m_joystick2.getRawAxis(0)) < .2)) {
                currentX = m_limeLight.tx();
                yError = m_limeLight.ty();
                if (Math.abs(yError) > 0) {
                    straightPower = 0.5 * Math.pow(Math.E, -0.10*Math.abs(currentX));
                } else {
                    straightPower = 0;
                }
                m_turnController.calculate( yError, true);
                pOut = m_turnController.getOutput();
                //System.out.println(pOut);
                if (!Double.isNaN( pOut )) {
                    turnAdjust = 0.7 * pOut;
                }
                if (index++ == 10000) {
                    index = 0;
                    System.out.println("y error: " + yError + " turn adjust: " + turnAdjust + " current x: " + currentX + " straight power: " + straightPower);
                }
                pOut = m_turnController.getOutput();
                //System.out.println(pOut);
                if (!Double.isNaN( pOut )) {
                    turnAdjust = pOut;
                }
                    if (turnAdjust < 0) {
                        m_drive.setDrive(straightPower - Math.abs(turnAdjust), straightPower + Math.abs(turnAdjust));
                    } else {
                    m_drive.setDrive(straightPower + Math.abs(turnAdjust), straightPower - Math.abs(turnAdjust));
                    // System.out.println("straightPower + turn adjust " + (straightPower + turnAdjust) + (straightPower - turnAdjust));
                    }
            }
            callSubroutine(new Actuate());
            m_drive.setDrive(.3, .3);
            waitForSeconds(0.4);
            m_drive.setDrive(0, 0);
            //waitForSeconds(0.5);
            callSubroutine(new RetractGripper());
            //waitForSeconds(0.2);
            m_drive.setDrive(-0.3 ,-0.3);
            waitForSeconds(0.7);
            callSubroutine(new Retract());
            m_drive.setDrive(0.0 , 0.0);

            /*
            waitForSeconds(0.05);
            callSubroutine(new RetractGripper());
            //waitForSeconds(0.2);
            drive.setDrive(-0.3 ,-0.3  , ControlMode.PercentOutput);
            waitForSeconds(0.05);
            callSubroutine(new ExtendGripper());
            callSubroutine(new Retract());
            drive.setDrive(0.0 , 0.0  , ControlMode.PercentOutput);
            */

            //callSubroutine(new PreciseDrive(drive.getGyroAngle(), -1.0, 0.7, 0));


    }








/*
        double distanceToTarget = limeLight.distanceToTarget(1, limeLight.tx());
        double targetAngle = limeLight.ty();
        m_turnController.setSetpoint(0.0);
        System.out.println("I shall seek and destroy/put on a hatch on something");
        while((distanceToTarget > m_endDistance) && !Robot.m_oi.driverControl) {
            distanceToTarget = limeLight.distanceToTarget(1, targetAngle);
            m_turnController.calculate(drive.AngleDifference(drive.getGyroAngle(), limeLight.ty()), true);
            double turnPower = m_turnController.getOutput();
            double straightPower = calcPower(Math.abs(getCurrentDistance() / distanceToTarget));
            if (turnPower > 0) {
                drive.setDrive(straightPower - turnPower, straightPower, ControlMode.PercentOutput);
            } else {
                drive.setDrive(straightPower, straightPower + turnPower, ControlMode.PercentOutput);
            }
            if (index % 15 == 0 && drive.isEnabled()) {
                System.out.println("TA: " + limeLight.ty() + " CA: " + drive.getGyroAngle() + " Diff: " + drive.AngleDifference(drive.getGyroAngle(), limeLight.ty()) + " Pout: " + m_turnController.getOutput() + " Power: " + calcPower(Math.abs(getCurrentDistance() / distanceToTarget)) + " Dist to Target: " + distanceToTarget + " Left Power: " + drive.leftMotorVelocity() + " Right Power: " + drive.rightMotorVelocity());
            }
            index++;
            if (!drive.isEnabled()){
                drive.nukeRobot();
                m_turnController.reset();
                return;
            }
        }
        System.out.println("PreciseDrive finished");
        drive.setDrive(m_endPower, m_endPower, ControlMode.PercentOutput);
        drive.resetEncoders();
        yield();
        return;
    }
    */

    public double getCurrentDistance() {
        return(((m_drive.rightEncoderDistance() + m_drive.leftEncoderDistance()) * m_drive.getDistPerPulse() )/2.0);
    }

  //  private double calcPower(double arcPercent) {
  //      if (arcPercent < END_POWER_PERCENT) {
  //          return m_targetPower;
  //      }
  //      double scaledPower = (1 - (arcPercent - END_POWER_PERCENT) / (1 - END_POWER_PERCENT)) * m_targetPower;
  //      if (m_targetPower >= 0) {
  //          return Math.max(MIN_POWER, scaledPower);
  //      } else {
  //          return Math.min(-MIN_POWER, scaledPower);
  //      }
  //  }

}