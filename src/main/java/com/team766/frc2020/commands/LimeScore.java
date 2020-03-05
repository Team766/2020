package com.team766.frc2020.commands;

import com.team766.controllers.TimeProviderI;
import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.mechanisms.Drive;
import com.team766.frc2020.mechanisms.DriveI;
import com.team766.frc2020.mechanisms.LimeLight;
import com.team766.frc2020.mechanisms.LimeLightI;
import com.team766.frc2020.mechanisms.LimeLight.CameraMode;
import com.team766.frc2020.mechanisms.LimeLight.LightMode;
import com.team766.hal.CANSpeedController.ControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.team766.controllers.PIDController;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;

public class LimeScore extends Subroutine {

    PIDController m_turnController;
    double m_targetPower;
    double m_endPower;
    double m_endDistance;
   // double MIN_POWER = 0.25;
    //double POWER_RAMP = 1.0;
  //  double END_POWER_PERCENT = 0.75;
    // int index = 0;
    double yError;
    double currentX;
    double turnAdjust;
    double straightPower;
    double pOut;
    DriveI m_drive;
    LimeLightI m_limeLight;
    public LimeScore() {
        m_drive = Robot.drive;
        m_limeLight = Robot.limeLight;
        m_turnController = new PIDController(LimeLight.P, LimeLight.I, LimeLight.D, LimeLight.THRESHOLD, RobotProvider.getTimeProvider() );
    }
    protected void subroutine() {
        System.out.println("hi");
        LimeLight.setLedMode(LightMode.eOn);
        LimeLight.setCameraMode(CameraMode.eVision);
        m_turnController.reset();
        m_drive.resetEncoders();
        currentX = m_limeLight.tx();
        yError = m_limeLight.ty();
        Robot.drive.setDrive(0,0);
        System.out.println("currentX"+ currentX);
        Robot.drive.setDrive(0,0);
       currentX = m_limeLight.tx();
         while (currentX<-0.7){
            Robot.drive.setDrive(-0.5,05);
          currentX = m_limeLight.tx();
          waitForSeconds(0.1);
          Robot.drive.setDrive(0,0);
        }
       Robot.drive.setDrive(0,0);
       currentX = m_limeLight.tx();
        while (currentX>0.7){
            Robot.drive.setDrive(0.5,-0.5);
            currentX = m_limeLight.tx();
            waitForSeconds(0.1);
            Robot.drive.setDrive(0,0);
        }

             while (currentX<-0.7){
            Robot.drive.setDrive(-0.5,0.5);
          currentX = m_limeLight.tx();
          waitForSeconds(0.1);
          Robot.drive.setDrive(0,0);
        }
        Robot.drive.setDrive(0,0);      
        currentX = m_limeLight.tx();
       System.out.println("currentX"+ currentX);
       System.out.println("Callibrated, if it ain't working, its a mechanical prolem, alos, Alex. C. and Anton and Jaob might be the reason it aint working, ask them questions cause its their fault entirely");
        yield();
        return;
    }
}